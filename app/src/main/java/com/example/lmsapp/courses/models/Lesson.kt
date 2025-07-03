package com.example.lmsapp.courses.models

import com.google.gson.annotations.SerializedName

enum class LessonType {
    @SerializedName("video") VIDEO,
    @SerializedName("pdf") PDF,
    @SerializedName("text") TEXT,
    @SerializedName("quiz") QUIZ,
    @SerializedName("assignment") ASSIGNMENT // Example, if assignments are part of lessons
}

data class Lesson(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String?,

    @SerializedName("order")
    val order: Int, // To maintain sequence within a section

    @SerializedName("type")
    val type: LessonType?, // VIDEO, PDF, QUIZ etc.

    @SerializedName("duration_minutes") // For video or estimated reading time
    val durationMinutes: Int?,

    @SerializedName("video_url")
    val videoUrl: String?,

    @SerializedName("pdf_url")
    val pdfUrl: String?,

    @SerializedName("content_url") // Generic content URL if not video/pdf specifically
    val contentUrl: String?,

    @SerializedName("content_text") // For text-based lessons
    val contentText: String?,

    @SerializedName("is_public") // If lesson can be public
    val isPublic: Boolean = false,

    @SerializedName("is_completed")
    val isCompleted: Boolean = false,

    @SerializedName("is_accessible") // Student-specific access
    val isAccessible: Boolean = true,

    @SerializedName("video_last_watched_position_seconds")
    val videoLastWatchedPositionSeconds: Long = 0L
)
