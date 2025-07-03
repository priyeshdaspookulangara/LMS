package com.example.ecommerceapp.data.repository;

import android.os.Handler;
import android.os.Looper;

import com.example.ecommerceapp.data.model.User; // Assuming User model

/**
 * A simulated repository for authentication.
 * In a real app, this would interact with a network API (e.g., Retrofit)
 * or a local database (e.g., Room for session management).
 */
public class AuthRepository {

    public interface AuthCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    // Simulate a network delay
    private static final int SIMULATED_DELAY_MS = 1500;
    private Handler handler = new Handler(Looper.getMainLooper());

    // Dummy user storage (in a real app, this would be your backend)
    private static User registeredUser = null;
    private static User loggedInUser = null;

    public AuthRepository() {
        // Initialize any necessary components (e.g., API service)
    }

    public void login(String email, String password, AuthCallback<User> callback) {
        handler.postDelayed(() -> {
            // Simulate API call for login
            if (registeredUser != null && registeredUser.getEmail().equals(email) && "password123".equals(password)) { // Simplified password check
                loggedInUser = new User(registeredUser.getUserId(), registeredUser.getUsername(), email, registeredUser.getFullName()); // Don't send password back
                callback.onSuccess(loggedInUser);
            } else {
                callback.onError("Invalid email or password.");
            }
        }, SIMULATED_DELAY_MS);
    }

    public void register(String username, String email, String password, AuthCallback<User> callback) {
        handler.postDelayed(() -> {
            // Simulate API call for registration
            if (registeredUser != null && registeredUser.getEmail().equals(email)) {
                callback.onError("Email already registered.");
                return;
            }
            // Simulate successful registration
            // In a real app, the backend would create a user ID.
            String newUserId = "user_" + System.currentTimeMillis();
            registeredUser = new User(newUserId, username, email, ""); // Full name can be added later
            // Typically, registration might not immediately log the user in, or it might.
            // For this simulation, we'll just signal success.
            callback.onSuccess(new User(newUserId, username, email, "")); // Return a representation of the new user
        }, SIMULATED_DELAY_MS);
    }

    public void logout() {
        // Simulate logout
        loggedInUser = null;
        // In a real app, you might also clear tokens, etc.
    }

    public User getCurrentUser() {
        return loggedInUser;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }
}
