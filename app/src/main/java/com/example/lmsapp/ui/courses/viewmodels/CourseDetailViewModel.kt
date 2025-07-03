package com.example.lmsapp.ui.courses.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lmsapp.courses.models.Course
import com.example.lmsapp.network.RetrofitInstance
import kotlinx.coroutines.launch

class CourseDetailViewModel : ViewModel() {

    private val _courseDetails = MutableLiveData<UiState<Course>>()
    val courseDetails: LiveData<UiState<Course>> = _courseDetails

    private val apiService = RetrofitInstance.api

    fun fetchCourseDetails(courseKey: String) {
        _courseDetails.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.getCourseDetails(courseKey)
                if (response.isSuccessful) {
                    response.body()?.let { course ->
                        _courseDetails.value = UiState.Success(course)
                    } ?: run {
                        _courseDetails.value = UiState.Error("Course data is null")
                    }
                } else {
                    _courseDetails.value = UiState.Error("Failed to load course details: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                _courseDetails.value = UiState.Error("Error fetching course details: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }
}
