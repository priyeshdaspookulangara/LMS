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
import android.content.Context; // For TokenManager
import androidx.annotation.NonNull; // For Retrofit Callbacks
import com.example.ecommerceapp.data.model.auth.AuthResponse;
import com.example.ecommerceapp.data.model.auth.LoginRequest;
import com.example.ecommerceapp.data.model.auth.RegisterRequest;
import com.example.ecommerceapp.network.ApiService;
import com.example.ecommerceapp.network.RetrofitClient;
import com.example.ecommerceapp.util.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for authentication. Interacts with network API.
 */
public class AuthRepository {

    public interface AuthCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    private final ApiService apiService;
    private final TokenManager tokenManager;
    private User currentUser; // Cache the current user details

    // Constructor now requires Context for TokenManager
    public AuthRepository(Context context) {
        this.apiService = RetrofitClient.getApiService();
        this.tokenManager = new TokenManager(context.getApplicationContext());
        // Attempt to load current user if a token exists (simplified)
        // In a real app, you might fetch user profile from API if token is valid
        // For now, if token exists, we assume user was logged in but details need to be re-fetched or were saved.
        // This simple caching of 'currentUser' will be cleared on logout.
        // A more robust solution would involve fetching /users/me on app start if token exists.
    }

    // Overloaded constructor for testing with mocks (if needed, though not used by ViewModel directly yet)
    public AuthRepository(ApiService apiService, TokenManager tokenManager) {
        this.apiService = apiService;
        this.tokenManager = tokenManager;
    }


    public void login(String email, String password, AuthCallback<User> callback) {
        apiService.loginUser(new LoginRequest(email, password)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.getAccessToken() != null && authResponse.getUser() != null) {
                        tokenManager.saveAccessToken(authResponse.getAccessToken());
                        currentUser = authResponse.getUser();
                        callback.onSuccess(currentUser);
                    } else {
                        callback.onError("Login failed: Invalid response from server.");
                    }
                } else {
                    // TODO: Parse errorBody for specific API error message
                    callback.onError("Login failed. Code: " + response.code() + " Msg: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                callback.onError("Network error during login: " + t.getMessage());
            }
        });
    }

    public void register(String username, String email, String password, String fullName, AuthCallback<User> callback) {
        apiService.registerUser(new RegisterRequest(username, email, password, fullName)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    // Assuming registration might also return a token and user details
                    if (authResponse.getUser() != null) {
                        if (authResponse.getAccessToken() != null) { // If token is returned on register
                            tokenManager.saveAccessToken(authResponse.getAccessToken());
                            currentUser = authResponse.getUser();
                        }
                        // Even if no token on register, success means user object might be returned.
                        callback.onSuccess(authResponse.getUser());
                    } else {
                        callback.onError("Registration failed: " + (authResponse.getMessage() != null ? authResponse.getMessage() : "Invalid response."));
                    }
                } else {
                     // TODO: Parse errorBody for specific API error message
                    callback.onError("Registration failed. Code: " + response.code() + " Msg: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                callback.onError("Network error during registration: " + t.getMessage());
            }
        });
    }

    public void logout() {
        tokenManager.clearToken();
        currentUser = null;
        // TODO: Call API /auth/logout if it exists and is required by the backend
    }

    public User getCurrentUser() {
        // This might be null if app was restarted and only token exists.
        // A robust implementation would fetch user details from /users/me if currentUser is null but token exists.
        return currentUser;
    }

    public String getAccessToken() {
        return tokenManager.getAccessToken();
    }

    public boolean isLoggedIn() {
        // Primary check is the presence of a token.
        // Optionally, also check if currentUser object is populated.
        return tokenManager.hasToken();
    }
}
