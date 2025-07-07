package com.example.ecommerceapp.data.model.auth;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters (and setters if needed, though typically request objects are immutable)
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
