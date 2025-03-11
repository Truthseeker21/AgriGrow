package com.agrigrow.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.agrigrow.database.AppDatabase;
import com.agrigrow.database.BadgeDao;
import com.agrigrow.database.GardeningChallengeDao;
import com.agrigrow.database.UserRewardDao;
import com.agrigrow.model.Badge;
import com.agrigrow.model.GardeningChallenge;
import com.agrigrow.model.UserReward;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Service for managing gamification features including challenges, badges, and rewards
 */
public class GamificationService {
    
    private static final String PREF_NAME = "gamification_prefs";
    private static final String KEY_USER_LEVEL = "user_level";
    private static final String KEY_USER_POINTS = "user_points";
    private static final String KEY_DAILY_STREAK = "daily_streak";
    private static final String KEY_LAST_LOGIN_DATE = "last_login_date";
    
    private final Context context;
    private final SharedPreferences preferences;
    private final BadgeDao badgeDao;
    private final GardeningChallengeDao challengeDao;
    private final UserRewardDao userRewardDao;
    private final Executor executor;
    
    // Current user ID (would be retrieved from user session in a real app)
    private final long currentUserId = 1;
    
    // Mutable live data for user stats
    private final MutableLiveData<Integer> userLevelLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> userPointsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> dailyStreakLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> newBadgeUnlockedLiveData = new MutableLiveData<>();
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    
    /**
     * Constructor for the GamificationService
     */
    public GamificationService(Context context) {
        this.context = context.getApplicationContext();
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        
        AppDatabase db = AppDatabase.getInstance(context);
        this.badgeDao = db.badgeDao();
        this.challengeDao = db.gardeningChallengeDao();
        this.userRewardDao = db.userRewardDao();
        
        this.executor = Executors.newSingleThreadExecutor();
        
        // Initialize live data with stored values
        loadUserStats();
    }
    
    /**
     * Load user stats from SharedPreferences
     */
    private void loadUserStats() {
        int userLevel = preferences.getInt(KEY_USER_LEVEL, 1);
        int userPoints = preferences.getInt(KEY_USER_POINTS, 0);
        int dailyStreak = preferences.getInt(KEY_DAILY_STREAK, 0);
        
        userLevelLiveData.setValue(userLevel);
        userPointsLiveData.setValue(userPoints);
        dailyStreakLiveData.setValue(dailyStreak);
        newBadgeUnlockedLiveData.setValue(false);
        
        // Check daily streak
        checkAndUpdateDailyStreak();
    }
    
    /**
     * Check and update the daily streak
     */
    private void checkAndUpdateDailyStreak() {
        String lastLoginDate = preferences.getString(KEY_LAST_LOGIN_DATE, null);
        String today = dateFormat.format(new Date());
        
        if (lastLoginDate != null) {
            try {
                Date lastLogin = dateFormat.parse(lastLoginDate);
                Date currentDate = new Date();
                
                // Calculate days between
                long diffInMillies = currentDate.getTime() - lastLogin.getTime();
                long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
                
                if (diffInDays == 1) {
                    // Increment streak
                    int currentStreak = preferences.getInt(KEY_DAILY_STREAK, 0);
                    currentStreak++;
                    preferences.edit().putInt(KEY_DAILY_STREAK, currentStreak).apply();
                    dailyStreakLiveData.setValue(currentStreak);
                    
                    // Check for streak-based badges
                    checkForStreakBadges(currentStreak);
                } else if (diffInDays > 1) {
                    // Reset streak
                    preferences.edit().putInt(KEY_DAILY_STREAK, 1).apply();
                    dailyStreakLiveData.setValue(1);
                }
            } catch (Exception e) {
                // Handle parse error
                preferences.edit().putInt(KEY_DAILY_STREAK, 1).apply();
                dailyStreakLiveData.setValue(1);
            }
        } else {
            // First login
            preferences.edit().putInt(KEY_DAILY_STREAK, 1).apply();
            dailyStreakLiveData.setValue(1);
        }
        
        // Update last login date
        preferences.edit().putString(KEY_LAST_LOGIN_DATE, today).apply();
    }
    
    /**
     * Check for badges based on streak count
     */
    private void checkForStreakBadges(int streakCount) {
        executor.execute(() -> {
            // For demonstration, check for daily streak badges
            // In a real app, this would be more complex with proper badge IDs
            if (streakCount == 3) {
                badgeDao.getNextAvailableBadges().observeForever(badges -> {
                    if (badges != null && !badges.isEmpty()) {
                        for (Badge badge : badges) {
                            if (badge.getCategory().equals("Streak") && 
                                    badge.getAchievementCriteria().contains("3 days")) {
                                unlockBadge(badge.getId());
                                break;
                            }
                        }
                    }
                });
            } else if (streakCount == 7) {
                badgeDao.getNextAvailableBadges().observeForever(badges -> {
                    if (badges != null && !badges.isEmpty()) {
                        for (Badge badge : badges) {
                            if (badge.getCategory().equals("Streak") && 
                                    badge.getAchievementCriteria().contains("7 days")) {
                                unlockBadge(badge.getId());
                                break;
                            }
                        }
                    }
                });
            } else if (streakCount == 30) {
                badgeDao.getNextAvailableBadges().observeForever(badges -> {
                    if (badges != null && !badges.isEmpty()) {
                        for (Badge badge : badges) {
                            if (badge.getCategory().equals("Streak") && 
                                    badge.getAchievementCriteria().contains("30 days")) {
                                unlockBadge(badge.getId());
                                break;
                            }
                        }
                    }
                });
            }
        });
    }
    
    /**
     * Add points to the user's account
     */
    public void addPoints(int points, String source) {
        if (points <= 0) return;
        
        executor.execute(() -> {
            // Update points in SharedPreferences
            int currentPoints = preferences.getInt(KEY_USER_POINTS, 0);
            int newPoints = currentPoints + points;
            preferences.edit().putInt(KEY_USER_POINTS, newPoints).apply();
            
            // Update LiveData on main thread
            userPointsLiveData.postValue(newPoints);
            
            // Create a point reward
            UserReward pointReward = new UserReward();
            pointReward.setUserId(currentUserId);
            pointReward.setRewardType("POINTS");
            pointReward.setRewardName(source);
            pointReward.setRewardDescription("Points earned from " + source);
            pointReward.setPointsEarned(points);
            pointReward.setDateEarned(new Date());
            pointReward.setRedeemed(true); // Points are automatically redeemed
            pointReward.setRedeemedDate(new Date());
            
            userRewardDao.insertUserReward(pointReward);
            
            // Check if user should level up
            checkForLevelUp(newPoints);
            
            // Check for point-based badges
            checkForPointBadges(newPoints);
        });
    }
    
    /**
     * Check if the user should level up based on points
     */
    private void checkForLevelUp(int points) {
        int currentLevel = preferences.getInt(KEY_USER_LEVEL, 1);
        int newLevel = calculateLevelFromPoints(points);
        
        if (newLevel > currentLevel) {
            // Level up!
            preferences.edit().putInt(KEY_USER_LEVEL, newLevel).apply();
            userLevelLiveData.postValue(newLevel);
            
            // Create a level-up reward
            UserReward levelUpReward = new UserReward();
            levelUpReward.setUserId(currentUserId);
            levelUpReward.setRewardType("LEVEL_UP");
            levelUpReward.setRewardName("Level " + newLevel);
            levelUpReward.setRewardDescription("You reached level " + newLevel + "!");
            levelUpReward.setPointsEarned(newLevel * 10); // Bonus points for leveling up
            levelUpReward.setDateEarned(new Date());
            levelUpReward.setRedeemed(true);
            levelUpReward.setRedeemedDate(new Date());
            levelUpReward.setFeatured(true);
            
            userRewardDao.insertUserReward(levelUpReward);
            
            // Check for newly available badges
            checkForLevelBadges(newLevel);
        }
    }
    
    /**
     * Calculate level based on points using a formula
     */
    private int calculateLevelFromPoints(int points) {
        // Simple formula: Level = sqrt(points / 25) + 1
        return (int) (Math.sqrt(points / 25.0) + 1);
    }
    
    /**
     * Check for badges based on user level
     */
    private void checkForLevelBadges(int level) {
        executor.execute(() -> {
            badgeDao.getAvailableBadgesForLevel(level).observeForever(badges -> {
                if (badges != null && !badges.isEmpty()) {
                    for (Badge badge : badges) {
                        unlockBadge(badge.getId());
                    }
                }
            });
        });
    }
    
    /**
     * Check for badges based on total points
     */
    private void checkForPointBadges(int points) {
        executor.execute(() -> {
            // For demonstration, check for point-based badges
            // In a real app, this would query badges with specific point thresholds
            if (points >= 100) {
                badgeDao.getNextAvailableBadges().observeForever(badges -> {
                    if (badges != null && !badges.isEmpty()) {
                        for (Badge badge : badges) {
                            if (badge.getCategory().equals("Points") && 
                                    badge.getAchievementCriteria().contains("100 points")) {
                                unlockBadge(badge.getId());
                                break;
                            }
                        }
                    }
                });
            } else if (points >= 500) {
                badgeDao.getNextAvailableBadges().observeForever(badges -> {
                    if (badges != null && !badges.isEmpty()) {
                        for (Badge badge : badges) {
                            if (badge.getCategory().equals("Points") && 
                                    badge.getAchievementCriteria().contains("500 points")) {
                                unlockBadge(badge.getId());
                                break;
                            }
                        }
                    }
                });
            } else if (points >= 1000) {
                badgeDao.getNextAvailableBadges().observeForever(badges -> {
                    if (badges != null && !badges.isEmpty()) {
                        for (Badge badge : badges) {
                            if (badge.getCategory().equals("Points") && 
                                    badge.getAchievementCriteria().contains("1000 points")) {
                                unlockBadge(badge.getId());
                                break;
                            }
                        }
                    }
                });
            }
        });
    }
    
    /**
     * Unlock a badge for the user
     */
    public void unlockBadge(long badgeId) {
        executor.execute(() -> {
            // Get the badge
            badgeDao.getBadgeById(badgeId).observeForever(badge -> {
                if (badge != null && !badge.isUnlocked()) {
                    // Update badge status
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(new Date());
                    badgeDao.unlockBadge(badgeId, currentDate);
                    
                    // Create a reward for the badge
                    UserReward badgeReward = new UserReward();
                    badgeReward.setUserId(currentUserId);
                    badgeReward.setRewardType("BADGE");
                    badgeReward.setRewardReferenceId(badgeId);
                    badgeReward.setRewardName(badge.getName());
                    badgeReward.setRewardDescription(badge.getDescription());
                    badgeReward.setPointsEarned(badge.getPointsAwarded());
                    badgeReward.setDateEarned(new Date());
                    badgeReward.setRewardImagePath(badge.getImagePath());
                    badgeReward.setFeatured(true);
                    
                    userRewardDao.insertUserReward(badgeReward);
                    
                    // Add points for the badge
                    addPoints(badge.getPointsAwarded(), "Badge: " + badge.getName());
                    
                    // Notify that a new badge was unlocked
                    newBadgeUnlockedLiveData.postValue(true);
                }
            });
        });
    }
    
    /**
     * Complete a challenge
     */
    public void completeChallenge(long challengeId) {
        executor.execute(() -> {
            // Get the challenge
            challengeDao.getChallengeById(challengeId).observeForever(challenge -> {
                if (challenge != null && !challenge.isCompleted()) {
                    // Mark challenge as completed
                    Date completionDate = new Date();
                    challengeDao.markChallengeAsCompleted(challengeId, completionDate);
                    
                    // Create a reward for the challenge
                    UserReward challengeReward = new UserReward();
                    challengeReward.setUserId(currentUserId);
                    challengeReward.setRewardType("CHALLENGE");
                    challengeReward.setRewardReferenceId(challengeId);
                    challengeReward.setRewardName(challenge.getTitle());
                    challengeReward.setRewardDescription(challenge.getDescription());
                    challengeReward.setPointsEarned(challenge.getPointsValue());
                    challengeReward.setDateEarned(completionDate);
                    challengeReward.setRewardImagePath(challenge.getBadgeImagePath());
                    challengeReward.setFeatured(true);
                    
                    userRewardDao.insertUserReward(challengeReward);
                    
                    // Add points for the challenge
                    addPoints(challenge.getPointsValue(), "Challenge: " + challenge.getTitle());
                    
                    // Check for challenge-related badges
                    checkForChallengeBadges();
                }
            });
        });
    }
    
    /**
     * Check for badges related to completing challenges
     */
    private void checkForChallengeBadges() {
        executor.execute(() -> {
            challengeDao.getCompletedChallengesCount().observeForever(count -> {
                if (count != null) {
                    if (count == 1) {
                        // First challenge completed badge
                        badgeDao.getNextAvailableBadges().observeForever(badges -> {
                            if (badges != null && !badges.isEmpty()) {
                                for (Badge badge : badges) {
                                    if (badge.getCategory().equals("Challenges") && 
                                            badge.getAchievementCriteria().contains("first challenge")) {
                                        unlockBadge(badge.getId());
                                        break;
                                    }
                                }
                            }
                        });
                    } else if (count == 5) {
                        // 5 challenges completed badge
                        badgeDao.getNextAvailableBadges().observeForever(badges -> {
                            if (badges != null && !badges.isEmpty()) {
                                for (Badge badge : badges) {
                                    if (badge.getCategory().equals("Challenges") && 
                                            badge.getAchievementCriteria().contains("5 challenges")) {
                                        unlockBadge(badge.getId());
                                        break;
                                    }
                                }
                            }
                        });
                    } else if (count == 10) {
                        // 10 challenges completed badge
                        badgeDao.getNextAvailableBadges().observeForever(badges -> {
                            if (badges != null && !badges.isEmpty()) {
                                for (Badge badge : badges) {
                                    if (badge.getCategory().equals("Challenges") && 
                                            badge.getAchievementCriteria().contains("10 challenges")) {
                                        unlockBadge(badge.getId());
                                        break;
                                    }
                                }
                            }
                        });
                    }
                }
            });
        });
    }
    
    /**
     * Redeem a reward
     */
    public void redeemReward(long rewardId) {
        executor.execute(() -> {
            Date redemptionDate = new Date();
            userRewardDao.markRewardAsRedeemed(rewardId, redemptionDate);
        });
    }
    
    /**
     * Join a challenge
     */
    public void joinChallenge(long challengeId) {
        executor.execute(() -> {
            challengeDao.incrementParticipantCount(challengeId);
        });
    }
    
    // Getter methods for LiveData objects
    
    public LiveData<Integer> getUserLevel() {
        return userLevelLiveData;
    }
    
    public LiveData<Integer> getUserPoints() {
        return userPointsLiveData;
    }
    
    public LiveData<Integer> getDailyStreak() {
        return dailyStreakLiveData;
    }
    
    public LiveData<Boolean> getNewBadgeUnlocked() {
        return newBadgeUnlockedLiveData;
    }
    
    // Method to reset the new badge notification
    public void resetNewBadgeNotification() {
        newBadgeUnlockedLiveData.setValue(false);
    }
    
    // Database access methods
    
    public LiveData<List<GardeningChallenge>> getActiveChallenges() {
        return challengeDao.getActiveChallenges();
    }
    
    public LiveData<List<GardeningChallenge>> getCompletedChallenges() {
        return challengeDao.getCompletedChallenges();
    }
    
    public LiveData<List<Badge>> getUnlockedBadges() {
        return badgeDao.getUnlockedBadges();
    }
    
    public LiveData<List<Badge>> getLockedBadges() {
        return badgeDao.getLockedBadges();
    }
    
    public LiveData<List<UserReward>> getUserRewards() {
        return userRewardDao.getUserRewardsByUserId(currentUserId);
    }
    
    public LiveData<List<UserReward>> getUnredeemedRewards() {
        return userRewardDao.getUnredeemedRewards(currentUserId);
    }
    
    public int getPointsToNextLevel() {
        int currentLevel = userLevelLiveData.getValue() != null ? userLevelLiveData.getValue() : 1;
        int currentPoints = userPointsLiveData.getValue() != null ? userPointsLiveData.getValue() : 0;
        
        // Calculate points needed for next level
        int nextLevelPoints = (int) (Math.pow(currentLevel, 2) * 25);
        return nextLevelPoints - currentPoints;
    }
    
    public int getLevelProgress() {
        int currentLevel = userLevelLiveData.getValue() != null ? userLevelLiveData.getValue() : 1;
        int currentPoints = userPointsLiveData.getValue() != null ? userPointsLiveData.getValue() : 0;
        
        // Calculate points needed for current and next level
        int currentLevelPoints = (int) (Math.pow(currentLevel - 1, 2) * 25);
        int nextLevelPoints = (int) (Math.pow(currentLevel, 2) * 25);
        
        // Calculate progress percentage
        int pointsInLevel = currentPoints - currentLevelPoints;
        int pointsNeededForLevel = nextLevelPoints - currentLevelPoints;
        
        return (int) ((float) pointsInLevel / pointsNeededForLevel * 100);
    }
}