package com.example.lmsapp.ui.courses.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lmsapp.courses.models.Course
import com.example.lmsapp.network.RetrofitInstance
import kotlinx.coroutines.launch

// Enum to represent different UI states
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class CourseViewModel : ViewModel() {

    private val _courses = MutableLiveData<UiState<List<Course>>>()
    val courses: LiveData<UiState<List<Course>>> = _courses

    private val apiService = RetrofitInstance.api

    fun loadCourses(isGuest: Boolean) {
        _courses.value = UiState.Loading
        viewModelScope.launch {
            try {
                // TODO: Implement actual logic for guest vs authenticated user
                // For now, fetch all archived courses.
                // Guests should only see public courses - this filtering might happen
                // client-side if the API doesn't support it directly for this endpoint,
                // or ideally, the API provides a flag/parameter for public courses.
                val response = apiService.getArchivedCourses(limit = 20) // Default limit

                if (response.isSuccessful) {
                    var courseList = response.body()?.courses ?: emptyList()
                    if (isGuest) {
                        // Filter for public courses if the user is a guest
                        courseList = courseList.filter { it.isPublic }
                    }
                    _courses.value = UiState.Success(courseList)
                } else {
                    _courses.value = UiState.Error("Failed to load courses: ${response.message()}")
                }
            } catch (e: Exception) {
                _courses.value = UiState.Error("Error fetching courses: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }
}
