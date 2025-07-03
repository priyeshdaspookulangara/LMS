package com.example.lmsapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lmsapp.auth.AuthRepository
import com.example.lmsapp.ui.courses.viewmodels.UiState // Reusing UiState for consistency
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    private val _loginState = MutableLiveData<UiState<Boolean>>()
    val loginState: LiveData<UiState<Boolean>> = _loginState

    fun login(passkey: String) {
        if (passkey.isBlank()) {
            _loginState.value = UiState.Error("Passkey cannot be empty.")
            return
        }

        _loginState.value = UiState.Loading
        viewModelScope.launch {
            // Simulate network delay or actual API call
            // kotlinx.coroutines.delay(1000)

            // TODO: Replace with actual API call to validate passkey and get a real token
            // For now, if passkey is not empty, consider it a successful login
            // and use the passkey itself as a dummy token.
            try {
                // Simulate successful login
                val dummyToken = "dummy_token_for_${passkey}"
                authRepository.saveAuthToken(dummyToken)
                _loginState.value = UiState.Success(true)
            } catch (e: Exception) {
                // Handle exceptions from saving token, though unlikely for DataStore basic ops
                _loginState.value = UiState.Error("Login failed: ${e.message}")
            }
        }
    }

    // Call this to reset the state, e.g., after navigation
    fun onLoginAttemptComplete() {
        _loginState.value = null // Or a specific "Idle" state if preferred
    }
}
