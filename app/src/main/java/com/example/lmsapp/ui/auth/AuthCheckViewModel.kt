package com.example.lmsapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lmsapp.auth.AuthRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AuthCheckViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    private val _navigationCommand = MutableLiveData<NavigationCommand?>()
    val navigationCommand: LiveData<NavigationCommand?> = _navigationCommand

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            // Attempt to get the token. Using firstOrNull for a one-time check at startup.
            // In more complex scenarios, you might want to continuously observe the token.
            val token = authRepository.authToken.firstOrNull()

            // Basic check: if token is not null and not empty, consider authenticated.
            // Real validation (e.g., checking expiry against a server) would be more complex.
            if (!token.isNullOrEmpty()) {
                _navigationCommand.value = NavigationCommand.ToCourseList
            } else {
                _navigationCommand.value = NavigationCommand.ToLoginRegister
            }
        }
    }

    fun onNavigationComplete() {
        _navigationCommand.value = null
    }

    sealed class NavigationCommand {
        object ToLoginRegister : NavigationCommand()
        object ToCourseList : NavigationCommand() // This will imply isGuest = false
    }
}
