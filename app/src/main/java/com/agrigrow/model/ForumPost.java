package com.agrigrow.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model class representing a post in the community forum.
 */
public class ForumPost {
    private int id;
    private String title;
    private String content;
    private String authorId;
    private String authorName;
    private String authorImageUrl;
    private Date timestamp;
    private List<String> tags;
    private String imageUrl;
    private int likeCount;
    private int commentCount;
    private List<Comment> comments;
    private boolean isLikedByCurrentUser;

    // Default constructor
    public ForumPost() {
        this.comments = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    // Parameterized constructor
    public ForumPost(int id, String title, String content, String authorId, String authorName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.timestamp = new Date();
        this.comments = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.likeCount = 0;
        this.commentCount = 0;
        this.isLikedByCurrentUser = false;
    }

    // Nested Comment class
    public static class Comment {
        private int id;
        private String content;
        private String authorId;
        private String authorName;
        private String authorImageUrl;
        private Date timestamp;
        private int likeCount;
        private boolean isLikedByCurrentUser;

        // Default constructor
        public Comment() {
        }

        // Parameterized constructor
        public Comment(int id, String content, String authorId, String authorName) {
            this.id = id;
            this.content = content;
            this.authorId = authorId;
            this.authorName = authorName;
            this.timestamp = new Date();
            this.likeCount = 0;
            this.isLikedByCurrentUser = false;
        }

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthorId() {
            return authorId;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorImageUrl() {
            return authorImageUrl;
        }

        public void setAuthorImageUrl(String authorImageUrl) {
            this.authorImageUrl = authorImageUrl;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public boolean isLikedByCurrentUser() {
            return isLikedByCurrentUser;
        }

        public void setLikedByCurrentUser(boolean likedByCurrentUser) {
            isLikedByCurrentUser = likedByCurrentUser;
        }
    }

    // Add a comment
    public void addComment(Comment comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
        this.commentCount = this.comments.size();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentCount = comments != null ? comments.size() : 0;
    }

    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        isLikedByCurrentUser = likedByCurrentUser;
    }
}
