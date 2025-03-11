package com.agrigrow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.agrigrow.model.ForumPost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for ForumPost related database operations.
 */
public class ForumPostDao {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    /**
     * Constructor initializing the database helper.
     */
    public ForumPostDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Open database connection.
     */
    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Close database connection.
     */
    private void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    /**
     * Get all forum posts from the database.
     */
    public List<ForumPost> getAllPosts() {
        List<ForumPost> posts = new ArrayList<>();
        open();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_FORUM_POSTS,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.KEY_TIMESTAMP + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ForumPost post = cursorToPost(cursor);
                // Load comments for each post
                post.setComments(getCommentsForPost(post.getId()));
                posts.add(post);
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return posts;
    }

    /**
     * Get forum posts by tag from the database.
     */
    public List<ForumPost> getPostsByTag(String tag) {
        List<ForumPost> posts = new ArrayList<>();
        open();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_FORUM_POSTS,
                null,
                DatabaseHelper.KEY_TAGS + " LIKE ?",
                new String[]{"%" + tag + "%"},
                null,
                null,
                DatabaseHelper.KEY_TIMESTAMP + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ForumPost post = cursorToPost(cursor);
                // Load comments for each post
                post.setComments(getCommentsForPost(post.getId()));
                posts.add(post);
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return posts;
    }

    /**
     * Get forum post by ID from the database.
     */
    public ForumPost getPost(int id) {
        open();
        ForumPost post = null;

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_FORUM_POSTS,
                null,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            post = cursorToPost(cursor);
            // Load comments for the post
            post.setComments(getCommentsForPost(id));
            cursor.close();
        }

        close();
        return post;
    }

    /**
     * Add a new forum post to the database.
     */
    public long addPost(ForumPost post) {
        open();
        
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_TITLE, post.getTitle());
        values.put(DatabaseHelper.KEY_CONTENT, post.getContent());
        values.put(DatabaseHelper.KEY_AUTHOR_ID, post.getAuthorId());
        values.put(DatabaseHelper.KEY_AUTHOR_NAME, post.getAuthorName());
        values.put(DatabaseHelper.KEY_AUTHOR_IMAGE_URL, post.getAuthorImageUrl());
        values.put(DatabaseHelper.KEY_TIMESTAMP, post.getTimestamp().getTime());
        
        // Convert list to comma-separated string
        if (post.getTags() != null && !post.getTags().isEmpty()) {
            values.put(DatabaseHelper.KEY_TAGS, TextUtils.join(",", post.getTags()));
        }
        
        values.put(DatabaseHelper.KEY_IMAGE_URL, post.getImageUrl());
        values.put(DatabaseHelper.KEY_LIKE_COUNT, post.getLikeCount());
        values.put(DatabaseHelper.KEY_COMMENT_COUNT, post.getCommentCount());
        values.put(DatabaseHelper.KEY_IS_LIKED, post.isLikedByCurrentUser() ? 1 : 0);
        
        long id = database.insert(DatabaseHelper.TABLE_FORUM_POSTS, null, values);
        
        // Set the generated ID to the post object
        if (id > 0) {
            post.setId((int) id);
            
            // Add comments if there are any
            if (post.getComments() != null && !post.getComments().isEmpty()) {
                for (ForumPost.Comment comment : post.getComments()) {
                    addComment(post.getId(), comment);
                }
            }
        }
        
        close();
        return id;
    }

    /**
     * Update post like status in the database.
     */
    public void updatePostLike(int id, boolean isLiked, int likeCount) {
        open();
        
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_IS_LIKED, isLiked ? 1 : 0);
        values.put(DatabaseHelper.KEY_LIKE_COUNT, likeCount);
        
        database.update(
                DatabaseHelper.TABLE_FORUM_POSTS,
                values,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        
        close();
    }

    /**
     * Get comments for a specific post.
     */
    private List<ForumPost.Comment> getCommentsForPost(int postId) {
        List<ForumPost.Comment> comments = new ArrayList<>();
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_COMMENTS,
                null,
                DatabaseHelper.KEY_POST_ID + " = ?",
                new String[]{String.valueOf(postId)},
                null,
                null,
                DatabaseHelper.KEY_TIMESTAMP + " ASC"
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ForumPost.Comment comment = cursorToComment(cursor);
                comments.add(comment);
            } while (cursor.moveToNext());
            
            cursor.close();
        }
        
        return comments;
    }

    /**
     * Add a comment to a post.
     */
    public long addComment(int postId, ForumPost.Comment comment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_POST_ID, postId);
        values.put(DatabaseHelper.KEY_CONTENT, comment.getContent());
        values.put(DatabaseHelper.KEY_AUTHOR_ID, comment.getAuthorId());
        values.put(DatabaseHelper.KEY_AUTHOR_NAME, comment.getAuthorName());
        values.put(DatabaseHelper.KEY_AUTHOR_IMAGE_URL, comment.getAuthorImageUrl());
        values.put(DatabaseHelper.KEY_TIMESTAMP, comment.getTimestamp().getTime());
        values.put(DatabaseHelper.KEY_LIKE_COUNT, comment.getLikeCount());
        values.put(DatabaseHelper.KEY_IS_LIKED, comment.isLikedByCurrentUser() ? 1 : 0);
        
        long id = database.insert(DatabaseHelper.TABLE_COMMENTS, null, values);
        
        // Update comment count in the post
        if (id > 0) {
            updateCommentCount(postId);
        }
        
        return id;
    }

    /**
     * Update comment count for a post.
     */
    private void updateCommentCount(int postId) {
        // Count the number of comments for this post
        Cursor cursor = database.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_COMMENTS + 
                " WHERE " + DatabaseHelper.KEY_POST_ID + " = ?",
                new String[]{String.valueOf(postId)}
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            
            // Update the post with the new comment count
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.KEY_COMMENT_COUNT, count);
            
            database.update(
                    DatabaseHelper.TABLE_FORUM_POSTS,
                    values,
                    DatabaseHelper.KEY_ID + " = ?",
                    new String[]{String.valueOf(postId)}
            );
            
            cursor.close();
        }
    }

    /**
     * Convert a database cursor to a ForumPost object.
     */
    private ForumPost cursorToPost(Cursor cursor) {
        ForumPost post = new ForumPost();
        
        int idIndex = cursor.getColumnIndex(DatabaseHelper.KEY_ID);
        int titleIndex = cursor.getColumnIndex(DatabaseHelper.KEY_TITLE);
        int contentIndex = cursor.getColumnIndex(DatabaseHelper.KEY_CONTENT);
        int authorIdIndex = cursor.getColumnIndex(DatabaseHelper.KEY_AUTHOR_ID);
        int authorNameIndex = cursor.getColumnIndex(DatabaseHelper.KEY_AUTHOR_NAME);
        int authorImageUrlIndex = cursor.getColumnIndex(DatabaseHelper.KEY_AUTHOR_IMAGE_URL);
        int timestampIndex = cursor.getColumnIndex(DatabaseHelper.KEY_TIMESTAMP);
        int tagsIndex = cursor.getColumnIndex(DatabaseHelper.KEY_TAGS);
        int imageUrlIndex = cursor.getColumnIndex(DatabaseHelper.KEY_IMAGE_URL);
        int likeCountIndex = cursor.getColumnIndex(DatabaseHelper.KEY_LIKE_COUNT);
        int commentCountIndex = cursor.getColumnIndex(DatabaseHelper.KEY_COMMENT_COUNT);
        int isLikedIndex = cursor.getColumnIndex(DatabaseHelper.KEY_IS_LIKED);
        
        if (idIndex != -1) post.setId(cursor.getInt(idIndex));
        if (titleIndex != -1) post.setTitle(cursor.getString(titleIndex));
        if (contentIndex != -1) post.setContent(cursor.getString(contentIndex));
        if (authorIdIndex != -1) post.setAuthorId(cursor.getString(authorIdIndex));
        if (authorNameIndex != -1) post.setAuthorName(cursor.getString(authorNameIndex));
        if (authorImageUrlIndex != -1) post.setAuthorImageUrl(cursor.getString(authorImageUrlIndex));
        
        if (timestampIndex != -1) {
            long timestamp = cursor.getLong(timestampIndex);
            post.setTimestamp(new Date(timestamp));
        }
        
        // Convert comma-separated string to list
        if (tagsIndex != -1 && !cursor.isNull(tagsIndex)) {
            String tagsStr = cursor.getString(tagsIndex);
            if (!TextUtils.isEmpty(tagsStr)) {
                post.setTags(Arrays.asList(tagsStr.split(",")));
            }
        }
        
        if (imageUrlIndex != -1) post.setImageUrl(cursor.getString(imageUrlIndex));
        if (likeCountIndex != -1) post.setLikeCount(cursor.getInt(likeCountIndex));
        if (commentCountIndex != -1) post.setCommentCount(cursor.getInt(commentCountIndex));
        if (isLikedIndex != -1) post.setLikedByCurrentUser(cursor.getInt(isLikedIndex) == 1);
        
        return post;
    }

    /**
     * Convert a database cursor to a Comment object.
     */
    private ForumPost.Comment cursorToComment(Cursor cursor) {
        ForumPost.Comment comment = new ForumPost.Comment();
        
        int idIndex = cursor.getColumnIndex(DatabaseHelper.KEY_ID);
        int contentIndex = cursor.getColumnIndex(DatabaseHelper.KEY_CONTENT);
        int authorIdIndex = cursor.getColumnIndex(DatabaseHelper.KEY_AUTHOR_ID);
        int authorNameIndex = cursor.getColumnIndex(DatabaseHelper.KEY_AUTHOR_NAME);
        int authorImageUrlIndex = cursor.getColumnIndex(DatabaseHelper.KEY_AUTHOR_IMAGE_URL);
        int timestampIndex = cursor.getColumnIndex(DatabaseHelper.KEY_TIMESTAMP);
        int likeCountIndex = cursor.getColumnIndex(DatabaseHelper.KEY_LIKE_COUNT);
        int isLikedIndex = cursor.getColumnIndex(DatabaseHelper.KEY_IS_LIKED);
        
        if (idIndex != -1) comment.setId(cursor.getInt(idIndex));
        if (contentIndex != -1) comment.setContent(cursor.getString(contentIndex));
        if (authorIdIndex != -1) comment.setAuthorId(cursor.getString(authorIdIndex));
        if (authorNameIndex != -1) comment.setAuthorName(cursor.getString(authorNameIndex));
        if (authorImageUrlIndex != -1) comment.setAuthorImageUrl(cursor.getString(authorImageUrlIndex));
        
        if (timestampIndex != -1) {
            long timestamp = cursor.getLong(timestampIndex);
            comment.setTimestamp(new Date(timestamp));
        }
        
        if (likeCountIndex != -1) comment.setLikeCount(cursor.getInt(likeCountIndex));
        if (isLikedIndex != -1) comment.setLikedByCurrentUser(cursor.getInt(isLikedIndex) == 1);
        
        return comment;
    }
}
