package com.example.lmsapp.courses.models

import com.google.gson.annotations.SerializedName

data class Section(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String?,

    @SerializedName("order")
    val order: Int, // To maintain sequence

    @SerializedName("lessons")
    val lessons: List<Lesson>? = null,

    @SerializedName("is_public") // If section access can be public even if course is not fully
    val isPublic: Boolean = false,

    @SerializedName("is_accessible") // Student-specific access, considering payment, etc.
    val isAccessible: Boolean = true
)
