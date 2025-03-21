
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

// Enable IR-based code generation and optimization
pragma abicoder v2;

/**
 * @custom:solidity-via-ir Enable IR-based code generation for deeper stack
 */

import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@openzeppelin/contracts/token/ERC20/utils/SafeERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";
import "@openzeppelin/contracts/security/Pausable.sol";
import "@uniswap/v2-core/contracts/interfaces/IUniswapV2Factory.sol";
import "@uniswap/v2-core/contracts/interfaces/IUniswapV2Pair.sol";
import "@uniswap/v2-periphery/contracts/interfaces/IUniswapV2Router02.sol";
import "@aave/core-v3/contracts/interfaces/IPool.sol";
import "@aave/core-v3/contracts/interfaces/IPoolAddressesProvider.sol";
import "@aave/core-v3/contracts/flashloan/interfaces/IFlashLoanReceiver.sol";

/**
 * @title EnhancedMEVBot
 * @dev Advanced MEV arbitrage bot with improved security features
 * and multi-DEX arbitrage capabilities
 */
contract EnhancedMEVBot is IFlashLoanReceiver, Ownable, ReentrancyGuard, Pausable {
    using SafeERC20 for IERC20;
    
    // Constants for frequently used addresses
    address private immutable WETH;
    address private immutable UNISWAP_ROUTER;
    address private immutable SUSHISWAP_ROUTER;
    address private immutable AAVE_LENDING_POOL;
    address private immutable AAVE_ADDRESSES_PROVIDER;

    // Additional DEX routers for more arbitrage opportunities
    mapping(string => address) public dexRouters;
    
    // Configuration parameters
    uint256 public slippageTolerance = 50; // 0.5%
    uint256 public minProfitThreshold = 0; // Minimum profit in base tokens to execute a trade
    uint256 public executionFee = 0; // Fee taken by the contract on successful arbitrage
    
    // Statistics tracking
    uint256 public totalExecutions;
    uint256 public totalProfit;
    uint256 public lastExecutionTimestamp;
    
    // Events
    event ArbitrageExecuted(
        address indexed token, 
        string sourceDex, 
        string targetDex, 
        uint256 amount, 
        uint256 profit
    );
    event FlashLoanReceived(address indexed asset, uint256 amount);
    event SlippageToleranceUpdated(uint256 oldValue, uint256 newValue);
    event MinProfitThresholdUpdated(uint256 oldValue, uint256 newValue);
    event ExecutionFeeUpdated(uint256 oldValue, uint256 newValue);
    event DexRouterAdded(string name, address router);
    event DexRouterRemoved(string name);

    /**
     * @dev Constructor to initialize the contract with required addresses
     * @param _weth WETH token address
     * @param _uniswapRouter Uniswap V2 router address
     * @param _sushiswapRouter Sushiswap router address
     * @param _aaveLendingPool Aave lending pool address
     * @param _aaveAddressesProvider Aave addresses provider
     */
    constructor(
        address _weth,
        address _uniswapRouter,
        address _sushiswapRouter,
        address _aaveLendingPool,
        address _aaveAddressesProvider
    ) Ownable(msg.sender) {
        require(_weth != address(0), "Invalid WETH address");
        require(_uniswapRouter != address(0), "Invalid Uniswap router");
        require(_sushiswapRouter != address(0), "Invalid Sushiswap router");
        require(_aaveLendingPool != address(0), "Invalid Aave pool");
        require(_aaveAddressesProvider != address(0), "Invalid Aave provider");
        
        WETH = _weth;
        UNISWAP_ROUTER = _uniswapRouter;
        SUSHISWAP_ROUTER = _sushiswapRouter;
        AAVE_LENDING_POOL = _aaveLendingPool;
        AAVE_ADDRESSES_PROVIDER = _aaveAddressesProvider;
        
        // Initialize default DEX routers
        dexRouters["uniswap"] = _uniswapRouter;
        dexRouters["sushiswap"] = _sushiswapRouter;
    }

    receive() external payable {}

    // Implement required IFlashLoanReceiver interface functions
    function ADDRESSES_PROVIDER() external view override returns (IPoolAddressesProvider) {
        return IPoolAddressesProvider(AAVE_ADDRESSES_PROVIDER);
    }

    function POOL() external view override returns (IPool) {
        return IPool(AAVE_LENDING_POOL);
    }

    /**
     * @dev Simulate arbitrage to check if it would be profitable
     * @param token Token to arbitrage
     * @param amount Amount to flash loan
     * @param sourceDex Source DEX to buy from
     * @param targetDex Target DEX to sell to
     * @return expectedProfit Expected profit from this arbitrage
     * @return isProfitable Whether the arbitrage would be profitable
     */
    function simulateArbitrage(
        address token, 
        uint256 amount,
        string calldata sourceDex,
        string calldata targetDex
    ) external view returns (uint256 expectedProfit, bool isProfitable) {
        require(dexRouters[sourceDex] != address(0), "Source DEX not configured");
        require(dexRouters[targetDex] != address(0), "Target DEX not configured");
        
        address sourceRouter = dexRouters[sourceDex];
        address targetRouter = dexRouters[targetDex];
        
        // Calculate prices on both DEXes
        uint256 sourcePrice = getPriceOnDex(token, amount, sourceRouter);
        uint256 targetPrice = getPriceOnDex(token, amount, targetRouter);
        
        // Calculate flash loan fee
        uint256 flashLoanFee = (amount * 9) / 10000; // Aave charges 0.09%
        
        // Check if arbitrage would be profitable
        if (targetPrice > sourcePrice + flashLoanFee) {
            expectedProfit = targetPrice - sourcePrice - flashLoanFee;
            isProfitable = expectedProfit > minProfitThreshold;
        } else {
            expectedProfit = 0;
            isProfitable = false;
        }
        
        return (expectedProfit, isProfitable);
    }

    /**
     * @dev Execute an arbitrage opportunity between two DEXes
     * @param token Token to arbitrage
     * @param amount Amount to flash loan
     * @param sourceDex Source DEX to buy from
     * @param targetDex Target DEX to sell to
     */
    function executeArbitrage(
        address token, 
        uint256 amount,
        string calldata sourceDex,
        string calldata targetDex
    ) external onlyOwner whenNotPaused nonReentrant {
        require(amount > 0, "Amount must be greater than 0");
        require(dexRouters[sourceDex] != address(0), "Source DEX not configured");
        require(dexRouters[targetDex] != address(0), "Target DEX not configured");
        
        // Encode operation parameters for the callback
        bytes memory params = abi.encode(
            msg.sender,
            sourceDex,
            targetDex
        );
        
        // Setup flash loan
        address[] memory assets = new address[](1);
        assets[0] = token;
        
        uint256[] memory amounts = new uint256[](1);
        amounts[0] = amount;
        
        uint256[] memory modes = new uint256[](1);
        modes[0] = 0; // 0 = no debt (flash loan)
        
        // Request flash loan
        IPool(AAVE_LENDING_POOL).flashLoan(
            address(this),
            assets,
            amounts,
            modes,
            address(this),
            params,
            0
        );
    }

    /**
     * @dev Flash loan callback function to execute arbitrage
     * @param assets Assets received
     * @param amounts Amounts received
     * @param premiums Flash loan premiums
     * @param initiator Flash loan initiator
     * @param params Additional parameters
     * @return success Whether the operation was successful
     */
    // Split the executeOperation to reduce stack variables
    function _performSwaps(
        address token,
        uint256 amount,
        uint256 totalDebt,
        address sourceRouter,
        address targetRouter
    ) private returns (uint256 receivedToken) {
        // Execute the first swap (token -> WETH)
        IERC20(token).approve(sourceRouter, amount);
        
        address[] memory pathSource = new address[](2);
        pathSource[0] = token;
        pathSource[1] = WETH;
        
        uint256 buyPrice = getPriceOnDex(token, amount, sourceRouter);
        uint256 minAmountOut = (buyPrice * (10000 - slippageTolerance)) / 10000;
        
        uint256[] memory amountsOut = IUniswapV2Router02(sourceRouter).swapExactTokensForTokens(
            amount,
            minAmountOut,
            pathSource,
            address(this),
            block.timestamp
        );
        
        // Execute the second swap (WETH -> token)
        uint256 receivedWETH = amountsOut[1];
        IERC20(WETH).approve(targetRouter, receivedWETH);
        
        address[] memory pathTarget = new address[](2);
        pathTarget[0] = WETH;
        pathTarget[1] = token;
        
        uint256 minReturnAmount = (totalDebt * (10000 + slippageTolerance)) / 10000;
        
        uint256[] memory returnAmounts = IUniswapV2Router02(targetRouter).swapExactTokensForTokens(
            receivedWETH,
            minReturnAmount,
            pathTarget,
            address(this),
            block.timestamp
        );
        
        return returnAmounts[1];
    }
    
    // Helper function to validate and prepare arbitrage parameters
    function _validateAndPrepare(
        address[] calldata assets,
        uint256[] calldata amounts,
        uint256[] calldata premiums,
        address initiator,
        bytes calldata params
    ) private view returns (
        address token,
        uint256 amount,
        uint256 totalDebt,
        address sourceRouter,
        address targetRouter
    ) {
        require(msg.sender == AAVE_LENDING_POOL, "Caller must be Aave lending pool");
        require(assets.length == 1 && amounts.length == 1, "Only single asset supported");
        
        // Decode and validate
        (address caller, string memory sourceDex, string memory targetDex) = 
            abi.decode(params, (address, string, string));
        require(caller == owner(), "Unauthorized initiator");
        
        token = assets[0];
        amount = amounts[0];
        totalDebt = amount + premiums[0];
        
        // Get routers
        sourceRouter = dexRouters[sourceDex];
        targetRouter = dexRouters[targetDex];
        
        return (token, amount, totalDebt, sourceRouter, targetRouter);
    }
    
    // Helper function to handle profit calculation and fee distribution
    function _processProfits(
        address token,
        uint256 totalDebt,
        uint256 receivedToken
    ) private returns (bool) {
        require(receivedToken >= totalDebt, "Insufficient for repayment");
        uint256 actualProfit = receivedToken - totalDebt;
        
        // Approve loan repayment
        IERC20(token).approve(AAVE_LENDING_POOL, totalDebt);
        
        // Update stats
        totalExecutions++;
        totalProfit += actualProfit;
        lastExecutionTimestamp = block.timestamp;
        
        // Handle fee distribution
        if (executionFee > 0 && actualProfit > 0) {
            uint256 fee = (actualProfit * executionFee) / 10000;
            if (fee > 0) {
                IERC20(token).safeTransfer(owner(), fee);
            }
        }
        
        return true;
    }

    function executeOperation(
        address[] calldata assets,
        uint256[] calldata amounts,
        uint256[] calldata premiums,
        address initiator,
        bytes calldata params
    ) external override nonReentrant returns (bool) {
        // Split validation and execution to reduce stack variables
        (
            address token,
            uint256 amount,
            uint256 totalDebt,
            address sourceRouter,
            address targetRouter
        ) = _validateAndPrepare(assets, amounts, premiums, initiator, params);
        
        emit FlashLoanReceived(token, amount);
        
        // Validate profitability
        uint256 tokenPrice = getPriceOnDex(token, amount, sourceRouter);
        uint256 sellPrice = getPriceOnDex(WETH, tokenPrice, targetRouter);
        require(sellPrice > totalDebt, "Not profitable");
        
        uint256 expectedProfit = sellPrice - totalDebt;
        require(expectedProfit > minProfitThreshold, "Profit below threshold");
        
        // Execute swaps
        uint256 receivedToken = _performSwaps(token, amount, totalDebt, sourceRouter, targetRouter);
        
        // Process profits and emit event
        bool success = _processProfits(token, totalDebt, receivedToken);
        
        // Extract source and target DEX from params for event
        (, string memory sourceDex, string memory targetDex) = 
            abi.decode(params, (address, string, string));
            
        emit ArbitrageExecuted(token, sourceDex, targetDex, amount, receivedToken - totalDebt);
        
        return success;
    }

    /**
     * @dev Get the price of a token on a specific DEX
     * @param token Token to check price for
     * @param amount Amount to get price for
     * @param router Router to use for price check
     * @return price Price of token on the DEX
     */
    function getPriceOnDex(
        address token,
        uint256 amount,
        address router
    ) public view returns (uint256) {
        address[] memory path = new address[](2);
        if (token == WETH) {
            path[0] = WETH;
            // Direct use of USDC address without symbol check
            path[1] = address(0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48); // USDC on mainnet
        } else {
            path[0] = token;
            path[1] = WETH;
        }
        
        try IUniswapV2Router02(router).getAmountsOut(amount, path) returns (uint256[] memory amountsOut) {
            return amountsOut[1];
        } catch {
            return 0;
        }
    }

    // Admin functions

    /**
     * @dev Add or update a DEX router
     * @param name Name of the DEX
     * @param router Address of the router
     */
    function addDexRouter(string calldata name, address router) external onlyOwner {
        require(router != address(0), "Router cannot be zero address");
        dexRouters[name] = router;
        emit DexRouterAdded(name, router);
    }

    /**
     * @dev Remove a DEX router
     * @param name Name of the DEX to remove
     */
    function removeDexRouter(string calldata name) external onlyOwner {
        require(dexRouters[name] != address(0), "Router does not exist");
        delete dexRouters[name];
        emit DexRouterRemoved(name);
    }

    /**
     * @dev Update slippage tolerance
     * @param _slippageTolerance New slippage tolerance (in basis points)
     */
    function setSlippageTolerance(uint256 _slippageTolerance) external onlyOwner {
        require(_slippageTolerance <= 1000, "Slippage tolerance too high"); // Max 10%
        emit SlippageToleranceUpdated(slippageTolerance, _slippageTolerance);
        slippageTolerance = _slippageTolerance;
    }

    /**
     * @dev Update minimum profit threshold
     * @param _minProfitThreshold New minimum profit threshold
     */
    function setMinProfitThreshold(uint256 _minProfitThreshold) external onlyOwner {
        emit MinProfitThresholdUpdated(minProfitThreshold, _minProfitThreshold);
        minProfitThreshold = _minProfitThreshold;
    }

    /**
     * @dev Update execution fee (in basis points)
     * @param _executionFee New execution fee 
     */
    function setExecutionFee(uint256 _executionFee) external onlyOwner {
        require(_executionFee <= 5000, "Fee too high"); // Max 50%
        emit ExecutionFeeUpdated(executionFee, _executionFee);
        executionFee = _executionFee;
    }

    /**
     * @dev Pause the contract
     */
    function pause() external onlyOwner {
        _pause();
    }

    /**
     * @dev Unpause the contract
     */
    function unpause() external onlyOwner {
        _unpause();
    }

    /**
     * @dev Emergency ETH withdrawal
     */
    function withdrawETH() external onlyOwner {
        uint256 balance = address(this).balance;
        require(balance > 0, "No ETH to withdraw");
        payable(owner()).transfer(balance);
    }

    /**
     * @dev Emergency token withdrawal
     * @param token Address of token to withdraw
     */
    function withdrawToken(address token) external onlyOwner {
        uint256 balance = IERC20(token).balanceOf(address(this));
        require(balance > 0, "No tokens to withdraw");
        IERC20(token).safeTransfer(owner(), balance);
    }
    
    /**
     * @dev Check contract has sufficient allowance to execute operations
     * @param token Token to check allowance for
     * @param router Router to check allowance for
     * @return bool Whether allowance is sufficient
     */
    function checkRouterAllowance(address token, address router) external view returns (bool) {
        uint256 allowance = IERC20(token).allowance(address(this), router);
        return allowance > 0;
    }
    
    /**
     * @dev Reset token allowance for a specific router
     * @param token Token to reset allowance for
     * @param router Router to reset allowance for
     */
    function resetAllowance(address token, address router) external onlyOwner {
        IERC20(token).approve(router, 0);
        IERC20(token).approve(router, type(uint256).max);
    }
}
