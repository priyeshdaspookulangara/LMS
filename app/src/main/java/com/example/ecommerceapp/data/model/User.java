package com.example.ecommerceapp.data.model;

import com.google.gson.annotations.SerializedName;
// Import other necessary classes like List<Address> if adding address list here later

public class User {

    @SerializedName("id") // Common API field name for user ID
    private String userId;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName") // Assuming API uses "fullName" or similar like "full_name"
    private String fullName;

    // Optional fields that might come from user profile API or auth response
    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("createdAt") // Example: "2023-10-27T10:00:00Z"
    private String createdAt; // Store as String, parse to Date if needed

    @SerializedName("updatedAt")
    private String updatedAt;

    // Password should NOT be part of this model if it's coming from an API response.
    // It's typically only sent TO the API for login/registration.

    // Default constructor for Gson
    public User() {}

    // Constructor for manual creation or testing
    public User(String userId, String username, String email, String fullName) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters (mainly for fields not set by Gson or for client-side updates)
    public void setUserId(String userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }


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
