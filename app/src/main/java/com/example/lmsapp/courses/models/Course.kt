package com.example.lmsapp.courses.models

import com.google.gson.annotations.SerializedName

data class Course(
    @SerializedName("id")
    val id: String, // Assuming an ID, could be Int

    @SerializedName("key")
    val key: String?, // From /courses/{key} endpoint

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("category")
    val category: String?,

    @SerializedName("instructor_name") // Example field, adjust as per actual API
    val instructorName: String?,

    @SerializedName("thumbnail_url") // Example field
    val thumbnailUrl: String?,

    @SerializedName("is_public") // To determine guest access
    val isPublic: Boolean = false,

    @SerializedName("total_lessons") // Example field
    val totalLessons: Int = 0,

    @SerializedName("progress_percent") // Example field for student progress
    val progressPercent: Int = 0
)

// It's common for list endpoints to return a wrapper object
data class CourseListResponse(
    @SerializedName("courses") // Assuming the list of courses is under a "courses" key
    val courses: List<Course>,

    @SerializedName("total_count") // Example field for pagination
    val totalCount: Int?,

    @SerializedName("limit") // Reflecting request param
    val limit: Int?,

    @SerializedName("offset") // Example field for pagination
    val offset: Int?
)
