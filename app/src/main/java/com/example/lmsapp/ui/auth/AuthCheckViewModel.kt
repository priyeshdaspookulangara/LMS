package com.example.lmsapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthCheckViewModel : ViewModel() {

    private val _navigationCommand = MutableLiveData<NavigationCommand?>()
    val navigationCommand: LiveData<NavigationCommand?> = _navigationCommand

    // In a real app, you'd inject a repository or use SharedPreferences/DataStore here
    // to check for a valid token.
    fun checkAuthState() {
        viewModelScope.launch {
            // Simulate a delay for checking auth status (e.g., reading from DataStore)
            delay(1500)

            // Placeholder logic: Assume user is not authenticated
            val isAuthenticated = false // TODO: Replace with actual auth check

            if (isAuthenticated) {
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
        object ToCourseList : NavigationCommand()
    }
}
