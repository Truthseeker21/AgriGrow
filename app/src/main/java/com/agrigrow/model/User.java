package com.agrigrow.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a user of the application.
 */
public class User {
    private String id;
    private String username;
    private String email;
    private String name;
    private String location;
    private String profileImageUrl;
    private String bio;
    private int experienceLevel; // 1-5 scale
    private List<Integer> savedPlants; // IDs of saved plants
    private List<Integer> savedGuides; // IDs of saved gardening guides
    private List<Integer> savedPosts; // IDs of saved forum posts
    private List<String> areasOfInterest; // e.g., "vegetables", "herbs", "flowers"
    private String gardenType; // e.g., "balcony", "backyard", "indoor"
    private int gardeningSpace; // in square feet
    private boolean notificationsEnabled;
    private String registrationDate;
    private int postCount;
    private int commentCount;
    private int points; // Gamification points

    // Default constructor
    public User() {
        this.savedPlants = new ArrayList<>();
        this.savedGuides = new ArrayList<>();
        this.savedPosts = new ArrayList<>();
        this.areasOfInterest = new ArrayList<>();
    }

    // Parameterized constructor with essential fields
    public User(String id, String username, String email, String name) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.savedPlants = new ArrayList<>();
        this.savedGuides = new ArrayList<>();
        this.savedPosts = new ArrayList<>();
        this.areasOfInterest = new ArrayList<>();
        this.experienceLevel = 1;
        this.postCount = 0;
        this.commentCount = 0;
        this.points = 0;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(int experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public List<Integer> getSavedPlants() {
        return savedPlants;
    }

    public void setSavedPlants(List<Integer> savedPlants) {
        this.savedPlants = savedPlants;
    }

    public List<Integer> getSavedGuides() {
        return savedGuides;
    }

    public void setSavedGuides(List<Integer> savedGuides) {
        this.savedGuides = savedGuides;
    }

    public List<Integer> getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(List<Integer> savedPosts) {
        this.savedPosts = savedPosts;
    }

    public List<String> getAreasOfInterest() {
        return areasOfInterest;
    }

    public void setAreasOfInterest(List<String> areasOfInterest) {
        this.areasOfInterest = areasOfInterest;
    }

    public String getGardenType() {
        return gardenType;
    }

    public void setGardenType(String gardenType) {
        this.gardenType = gardenType;
    }

    public int getGardeningSpace() {
        return gardeningSpace;
    }

    public void setGardeningSpace(int gardeningSpace) {
        this.gardeningSpace = gardeningSpace;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    // Convenience methods
    public void savePlant(int plantId) {
        if (!this.savedPlants.contains(plantId)) {
            this.savedPlants.add(plantId);
        }
    }

    public void saveGuide(int guideId) {
        if (!this.savedGuides.contains(guideId)) {
            this.savedGuides.add(guideId);
        }
    }

    public void savePost(int postId) {
        if (!this.savedPosts.contains(postId)) {
            this.savedPosts.add(postId);
        }
    }

    public void removeSavedPlant(int plantId) {
        this.savedPlants.remove(Integer.valueOf(plantId));
    }

    public void removeSavedGuide(int guideId) {
        this.savedGuides.remove(Integer.valueOf(guideId));
    }

    public void removeSavedPost(int postId) {
        this.savedPosts.remove(Integer.valueOf(postId));
    }

    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }
}
