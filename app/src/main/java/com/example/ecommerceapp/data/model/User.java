package com.example.ecommerceapp.data.model;

public class User {
    private String userId;
    private String username;
    private String email;
    private String fullName;
    // Add other fields as per your database_schema.md (address, phone_number, etc.)
    // For simplicity, only basic fields are included here.
    // Password hash should NOT be stored in the client-side User model.

    // Constructors
    public User(String userId, String username, String email, String fullName) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    // Setters (if needed, but often User objects are immutable or semi-immutable after creation)
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // toString, equals, hashCode (optional but good practice)
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
