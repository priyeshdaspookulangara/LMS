package com.example.ecommerceapp.ui.auth;

import android.app.Application; // Added for AndroidViewModel
import androidx.annotation.NonNull; // Added for AndroidViewModel constructor
import androidx.lifecycle.AndroidViewModel; // Changed from ViewModel
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
// import androidx.lifecycle.ViewModel; // Removed

import com.example.ecommerceapp.data.model.User;
import com.example.ecommerceapp.data.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel { // Changed to AndroidViewModel

    private AuthRepository authRepository;
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<RegistrationResult> registrationResult = new MutableLiveData<>();
    private MutableLiveData<User> authenticatedUser = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) { // Constructor for AndroidViewModel
        super(application);
        // Initialize AuthRepository with application context
        this.authRepository = new AuthRepository(application.getApplicationContext());
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public LiveData<RegistrationResult> getRegistrationResult() {
        return registrationResult;
    }

    public LiveData<User> getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void login(String email, String password) {
        // Simulate API call
        authRepository.login(email, password, new AuthRepository.AuthCallback<User>() {
            @Override
            public void onSuccess(User user) {
                authenticatedUser.setValue(user);
                loginResult.setValue(new LoginResult(true, null));
            }

            @Override
            public void onError(String error) {
                loginResult.setValue(new LoginResult(false, error));
            }
        });
    }

    public void register(String username, String email, String password, String fullName) { // Added fullName
        // API call
        authRepository.register(username, email, password, fullName, new AuthRepository.AuthCallback<User>() { // Pass fullName
            @Override
            public void onSuccess(User user) {
                // Potentially auto-login user or direct to login screen
                // For now, just indicate success
                registrationResult.setValue(new RegistrationResult(true, null));
            }

            @Override
            public void onError(String error) {
                registrationResult.setValue(new RegistrationResult(false, error));
            }
        });
    }

    public void logout() {
        // Simulate logout
        authRepository.logout();
        authenticatedUser.setValue(null); // Clear authenticated user
        // Optionally, notify UI about logout status if needed
    }
}

// Helper classes for results
class LoginResult {
    private boolean success;
    private String error;

    LoginResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}

class RegistrationResult {
    private boolean success;
    private String error;

    RegistrationResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
