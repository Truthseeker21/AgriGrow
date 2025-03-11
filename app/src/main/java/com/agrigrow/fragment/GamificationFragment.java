package com.agrigrow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.agrigrow.R;
import com.agrigrow.adapter.BadgeAdapter;
import com.agrigrow.adapter.GardeningChallengeAdapter;
import com.agrigrow.adapter.RewardAdapter;
import com.agrigrow.database.AppDatabase;
import com.agrigrow.database.BadgeDao;
import com.agrigrow.database.GardeningChallengeDao;
import com.agrigrow.database.UserRewardDao;
import com.agrigrow.model.Badge;
import com.agrigrow.model.GardeningChallenge;
import com.agrigrow.model.UserReward;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Fragment for gamification features including challenges, badges, and rewards
 */
public class GamificationFragment extends Fragment {

    // UI Components
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private LinearLayout userStatsLayout;
    private TextView levelTextView;
    private TextView totalPointsTextView;
    private TextView challengesCompletedTextView;
    private TextView badgesUnlockedTextView;
    private ProgressBar levelProgressBar;
    private TextView nextLevelPointsTextView;
    
    // Stats section
    private CardView activeCardView;
    private TextView activeChallengeCountTextView;
    private CardView badgesCardView;
    private TextView badgesCountTextView;
    private CardView rewardsCardView;
    private TextView rewardsCountTextView;
    
    // Challenges section
    private RecyclerView activeChallengesRecyclerView;
    private GardeningChallengeAdapter activeChallengesAdapter;
    private TextView noChallengesTextView;
    private ProgressBar challengesLoadingProgressBar;
    
    // End soon section
    private RecyclerView endingSoonChallengesRecyclerView;
    private GardeningChallengeAdapter endingSoonChallengesAdapter;
    private TextView noEndingSoonTextView;
    
    // Badges section
    private RecyclerView recentBadgesRecyclerView;
    private BadgeAdapter recentBadgesAdapter;
    private TextView noBadgesTextView;
    private ProgressBar badgesLoadingProgressBar;
    
    // Rewards section
    private RecyclerView rewardsRecyclerView;
    private RewardAdapter rewardsAdapter;
    private TextView noRewardsTextView;
    private ProgressBar rewardsLoadingProgressBar;
    
    // Database access
    private GardeningChallengeDao challengeDao;
    private BadgeDao badgeDao;
    private UserRewardDao userRewardDao;
    
    // Current user ID (would be retrieved from user session/preferences in a real app)
    private long currentUserId = 1;
    
    public GamificationFragment() {
        // Required empty constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gamification, container, false);
        initializeViews(view);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize database access
        AppDatabase db = AppDatabase.getInstance(requireContext());
        challengeDao = db.gardeningChallengeDao();
        badgeDao = db.badgeDao();
        userRewardDao = db.userRewardDao();
        
        setupAdapters();
        setupClickListeners();
        loadUserStats();
        loadActiveChallenges();
        loadEndingSoonChallenges();
        loadRecentBadges();
        loadRewards();
    }
    
    private void initializeViews(View view) {
        // Top level views
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        userStatsLayout = view.findViewById(R.id.layout_user_stats);
        
        // User stats
        levelTextView = view.findViewById(R.id.tv_level);
        totalPointsTextView = view.findViewById(R.id.tv_total_points);
        challengesCompletedTextView = view.findViewById(R.id.tv_challenges_completed);
        badgesUnlockedTextView = view.findViewById(R.id.tv_badges_unlocked);
        levelProgressBar = view.findViewById(R.id.progress_level);
        nextLevelPointsTextView = view.findViewById(R.id.tv_next_level_points);
        
        // Stats cards
        activeCardView = view.findViewById(R.id.card_active_challenges);
        activeChallengeCountTextView = view.findViewById(R.id.tv_active_challenge_count);
        badgesCardView = view.findViewById(R.id.card_badges);
        badgesCountTextView = view.findViewById(R.id.tv_badges_count);
        rewardsCardView = view.findViewById(R.id.card_rewards);
        rewardsCountTextView = view.findViewById(R.id.tv_rewards_count);
        
        // Challenges section
        activeChallengesRecyclerView = view.findViewById(R.id.recycler_active_challenges);
        noChallengesTextView = view.findViewById(R.id.tv_no_challenges);
        challengesLoadingProgressBar = view.findViewById(R.id.progress_challenges);
        
        // End soon section
        endingSoonChallengesRecyclerView = view.findViewById(R.id.recycler_ending_soon_challenges);
        noEndingSoonTextView = view.findViewById(R.id.tv_no_ending_soon);
        
        // Badges section
        recentBadgesRecyclerView = view.findViewById(R.id.recycler_recent_badges);
        noBadgesTextView = view.findViewById(R.id.tv_no_badges);
        badgesLoadingProgressBar = view.findViewById(R.id.progress_badges);
        
        // Rewards section
        rewardsRecyclerView = view.findViewById(R.id.recycler_rewards);
        noRewardsTextView = view.findViewById(R.id.tv_no_rewards);
        rewardsLoadingProgressBar = view.findViewById(R.id.progress_rewards);
    }
    
    private void setupAdapters() {
        // Active challenges adapter
        activeChallengesAdapter = new GardeningChallengeAdapter(requireContext(),
                challenge -> onChallengeClicked(challenge));
        activeChallengesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        activeChallengesRecyclerView.setAdapter(activeChallengesAdapter);
        
        // Ending soon challenges adapter
        endingSoonChallengesAdapter = new GardeningChallengeAdapter(requireContext(),
                challenge -> onChallengeClicked(challenge));
        endingSoonChallengesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        endingSoonChallengesRecyclerView.setAdapter(endingSoonChallengesAdapter);
        
        // Recent badges adapter
        recentBadgesAdapter = new BadgeAdapter(requireContext(),
                badge -> onBadgeClicked(badge));
        recentBadgesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recentBadgesRecyclerView.setAdapter(recentBadgesAdapter);
        
        // Rewards adapter
        rewardsAdapter = new RewardAdapter(requireContext(),
                reward -> onRewardClicked(reward));
        rewardsRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        rewardsRecyclerView.setAdapter(rewardsAdapter);
    }
    
    private void setupClickListeners() {
        // Stats cards click listeners
        activeCardView.setOnClickListener(v -> {
            viewPager.setCurrentItem(0, true);
        });
        
        badgesCardView.setOnClickListener(v -> {
            viewPager.setCurrentItem(1, true);
        });
        
        rewardsCardView.setOnClickListener(v -> {
            viewPager.setCurrentItem(2, true);
        });
    }
    
    private void loadUserStats() {
        // In a real app, this would load user stats from database or preferences
        // For now, set some example data
        levelTextView.setText(getString(R.string.level_value, 5));
        totalPointsTextView.setText(getString(R.string.points_value, 1250));
        
        // Load completed challenges count
        challengeDao.getCompletedChallengesCount().observe(getViewLifecycleOwner(), count -> {
            challengesCompletedTextView.setText(String.valueOf(count));
        });
        
        // Load unlocked badges count
        badgeDao.getUnlockedBadgesCount().observe(getViewLifecycleOwner(), count -> {
            badgesUnlockedTextView.setText(String.valueOf(count));
            badgesCountTextView.setText(String.valueOf(count));
        });
        
        // Set up level progress bar
        levelProgressBar.setMax(1000);
        levelProgressBar.setProgress(750);
        nextLevelPointsTextView.setText(getString(R.string.next_level_progress, 750, 1000));
    }
    
    private void loadActiveChallenges() {
        challengesLoadingProgressBar.setVisibility(View.VISIBLE);
        noChallengesTextView.setVisibility(View.GONE);
        activeChallengesRecyclerView.setVisibility(View.GONE);
        
        challengeDao.getActiveChallenges().observe(getViewLifecycleOwner(), challenges -> {
            challengesLoadingProgressBar.setVisibility(View.GONE);
            
            if (challenges == null || challenges.isEmpty()) {
                noChallengesTextView.setVisibility(View.VISIBLE);
                activeChallengesRecyclerView.setVisibility(View.GONE);
            } else {
                noChallengesTextView.setVisibility(View.GONE);
                activeChallengesRecyclerView.setVisibility(View.VISIBLE);
                activeChallengesAdapter.submitList(challenges);
                activeChallengeCountTextView.setText(String.valueOf(challenges.size()));
            }
        });
    }
    
    private void loadEndingSoonChallenges() {
        Date currentDate = Calendar.getInstance().getTime();
        
        challengeDao.getEndingSoonChallenges(currentDate).observe(getViewLifecycleOwner(), challenges -> {
            if (challenges == null || challenges.isEmpty()) {
                noEndingSoonTextView.setVisibility(View.VISIBLE);
                endingSoonChallengesRecyclerView.setVisibility(View.GONE);
            } else {
                noEndingSoonTextView.setVisibility(View.GONE);
                endingSoonChallengesRecyclerView.setVisibility(View.VISIBLE);
                endingSoonChallengesAdapter.submitList(challenges);
            }
        });
    }
    
    private void loadRecentBadges() {
        badgesLoadingProgressBar.setVisibility(View.VISIBLE);
        noBadgesTextView.setVisibility(View.GONE);
        recentBadgesRecyclerView.setVisibility(View.GONE);
        
        badgeDao.getRecentlyUnlockedBadges().observe(getViewLifecycleOwner(), badges -> {
            badgesLoadingProgressBar.setVisibility(View.GONE);
            
            if (badges == null || badges.isEmpty()) {
                noBadgesTextView.setVisibility(View.VISIBLE);
                recentBadgesRecyclerView.setVisibility(View.GONE);
            } else {
                noBadgesTextView.setVisibility(View.GONE);
                recentBadgesRecyclerView.setVisibility(View.VISIBLE);
                recentBadgesAdapter.submitList(badges);
            }
        });
    }
    
    private void loadRewards() {
        rewardsLoadingProgressBar.setVisibility(View.VISIBLE);
        noRewardsTextView.setVisibility(View.GONE);
        rewardsRecyclerView.setVisibility(View.GONE);
        
        userRewardDao.getUnredeemedRewards(currentUserId).observe(getViewLifecycleOwner(), rewards -> {
            rewardsLoadingProgressBar.setVisibility(View.GONE);
            
            if (rewards == null || rewards.isEmpty()) {
                noRewardsTextView.setVisibility(View.VISIBLE);
                rewardsRecyclerView.setVisibility(View.GONE);
            } else {
                noRewardsTextView.setVisibility(View.GONE);
                rewardsRecyclerView.setVisibility(View.VISIBLE);
                rewardsAdapter.submitList(rewards);
                rewardsCountTextView.setText(String.valueOf(rewards.size()));
            }
        });
    }
    
    private void onChallengeClicked(GardeningChallenge challenge) {
        // Show challenge details or navigate to challenge detail screen
        // In a real app, this would launch a detail activity or show a dialog
        // For now, just show a Toast
        // Toast.makeText(requireContext(), "Challenge: " + challenge.getTitle(), Toast.LENGTH_SHORT).show();
        
        // Example of showing a detail dialog
        showChallengeDetailsDialog(challenge);
    }
    
    private void onBadgeClicked(Badge badge) {
        // Show badge details or navigate to badge detail screen
        // In a real app, this would launch a detail activity or show a dialog
        // For now, just show a Toast
        // Toast.makeText(requireContext(), "Badge: " + badge.getName(), Toast.LENGTH_SHORT).show();
        
        // Example of showing a detail dialog
        showBadgeDetailsDialog(badge);
    }
    
    private void onRewardClicked(UserReward reward) {
        // Show reward details or redeem the reward
        // In a real app, this would launch a detail activity or show a dialog
        // For now, just show a Toast
        // Toast.makeText(requireContext(), "Reward: " + reward.getRewardName(), Toast.LENGTH_SHORT).show();
        
        // Example of showing a detail dialog
        showRewardDetailsDialog(reward);
    }
    
    private void showChallengeDetailsDialog(GardeningChallenge challenge) {
        // In a real app, show a dialog with challenge details
        // For this implementation, this would be handled by a custom DialogFragment
    }
    
    private void showBadgeDetailsDialog(Badge badge) {
        // In a real app, show a dialog with badge details
        // For this implementation, this would be handled by a custom DialogFragment
    }
    
    private void showRewardDetailsDialog(UserReward reward) {
        // In a real app, show a dialog with reward details
        // For this implementation, this would be handled by a custom DialogFragment
    }
}