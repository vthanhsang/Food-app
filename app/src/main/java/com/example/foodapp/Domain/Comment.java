package com.example.foodapp.Domain;



public class Comment {
    private String commentId;
    private String userId;
    private String userName;
    private String information;
    private String foodId;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String commentId, String userId, String userName, String information, String foodId) {
        this.commentId = commentId;
        this.userId = userId;
        this.userName = userName;
        this.information = information;
        this.foodId = foodId;
    }

    // Getters and setters
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
