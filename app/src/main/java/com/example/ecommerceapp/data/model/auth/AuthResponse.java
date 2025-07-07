package com.example.ecommerceapp.data.model.auth;

import com.example.ecommerceapp.data.model.User; // Assuming User model will be updated for API fields
import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("accessToken") // Or "token", "access_token", etc. - adjust to your API
    private String accessToken;

    @SerializedName("user")
    private User user; // The API might return a full User object

    @SerializedName("message") // For messages like "User registered successfully"
    private String message;

    // Default constructor for Gson
    public AuthResponse() {}

    public AuthResponse(String accessToken, User user, String message) {
        this.accessToken = accessToken;
        this.user = user;
        this.message = message;
    }

    // Getters
    public String getAccessToken() {
        return accessToken;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    // Setters (optional, if needed)
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
