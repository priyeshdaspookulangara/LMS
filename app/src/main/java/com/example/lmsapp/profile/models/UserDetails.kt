package com.example.lmsapp.profile.models

import com.google.gson.annotations.SerializedName

data class UserDetails(
    @SerializedName("user_id")
    val userId: String,

    @SerializedName("first_name")
    val firstName: String?,

    @SerializedName("last_name")
    val lastName: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("avatar_url")
    val avatarUrl: String?,

    @SerializedName("bio")
    val bio: String?,

    @SerializedName("date_of_birth") // Consider date format, e.g., "YYYY-MM-DD"
    val dateOfBirth: String?,

    @SerializedName("phone_number")
    val phoneNumber: String?,

    @SerializedName("address")
    val address: Address?,

    @SerializedName("registration_date") // ISO 8601 date string
    val registrationDate: String?,

    @SerializedName("last_login_date") // ISO 8601 date string
    val lastLoginDate: String?,

    // Preferences or settings specific to the user
    @SerializedName("prefers_dark_mode")
    val prefersDarkMode: Boolean? = false,

    @SerializedName("email_notifications_enabled")
    val emailNotificationsEnabled: Boolean? = true
)

data class Address(
    @SerializedName("street")
    val street: String?,

    @SerializedName("city")
    val city: String?,

    @SerializedName("state")
    val state: String?,

    @SerializedName("postal_code")
    val postalCode: String?,

    @SerializedName("country")
    val country: String?
)
