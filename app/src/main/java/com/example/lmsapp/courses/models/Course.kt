package com.example.lmsapp.courses.models

import com.google.gson.annotations.SerializedName

data class Course(
    @SerializedName("id")
    val id: String, // Assuming an ID, could be Int

    @SerializedName("key")
    val key: String?, // From /courses/{key} endpoint, likely the primary identifier for detail view

    @SerializedName("title")
    val title: String?,

    @SerializedName("short_description") // Short description for list views
    val shortDescription: String?,

    @SerializedName("full_description") // Detailed description for detail view
    val fullDescription: String?,

    @SerializedName("category")
    val category: String?,

    @SerializedName("instructor_name")
    val instructorName: String?, // Could be expanded into an Instructor object

    @SerializedName("instructor_details") // Placeholder for more detailed instructor info
    val instructorDetails: Instructor?,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?,

    @SerializedName("cover_image_url") // For course detail page
    val coverImageUrl: String?,

    @SerializedName("is_public") // To determine guest access
    val isPublic: Boolean = false,

    @SerializedName("is_enrolled")
    val isEnrolled: Boolean = false,

    @SerializedName("enrollment_status") // e.g., "active", "pending_payment", "expired"
    val enrollmentStatus: String?,

    @SerializedName("total_lessons")
    val totalLessons: Int = 0,

    @SerializedName("completed_lessons")
    val completedLessons: Int = 0,

    @SerializedName("progress_percent")
    val progressPercent: Int = 0,

    @SerializedName("rating")
    val rating: Double = 0.0,

    @SerializedName("total_ratings")
    val totalRatings: Int = 0,

    @SerializedName("sections") // List of sections within the course
    val sections: List<Section>? = null,

    @SerializedName("last_accessed_at")
    val lastAccessedAt: String? // ISO 8601 date string
)

data class Instructor(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?
)

// It's common for list endpoints to return a wrapper object
data class CourseListResponse(
    // Assuming the list of courses is under a "courses" key, or it might be a direct list
    @SerializedName("data") // Often API responses are wrapped in a "data" field
    val courses: List<Course>?,

    @SerializedName("count") // Example field for pagination
    val totalCount: Int?,

    // Other pagination fields like next_page_url, prev_page_url etc. might exist
    @SerializedName("limit") // Reflecting request param
    val limit: Int?,

    @SerializedName("page")
    val page: Int?,

    @SerializedName("total_pages")
    val totalPages: Int?
    // The original "offset" is less common than page-based pagination for REST APIs
)
