package com.example.foodapp.Domain;

public class User {
    private String name;
    private String email;
    private String password;
    private String userId;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String email, String password, String userId) {
        this.name = name; // Initialize name
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
