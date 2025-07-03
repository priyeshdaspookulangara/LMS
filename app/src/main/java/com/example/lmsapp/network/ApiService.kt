package com.example.lmsapp.network

import com.example.lmsapp.courses.models.Course
import com.example.lmsapp.courses.models.CourseListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /**
     * Retrieve a list of courses with filtering and sorting options.
     * Endpoint: /courses/archive-course
     * Method: GET
     * Permissions: public
     */
    @GET("courses/archive-course")
    suspend fun getArchivedCourses(
        @Query("limit") limit: Int? = null,
        @Query("order_by") orderBy: String? = null, // e.g., "title", "date_created"
        @Query("order") order: String? = null,     // e.g., "asc", "desc"
        @Query("category") category: String? = null
    ): Response<CourseListResponse> // Using CourseListResponse as defined earlier

    /**
     * Get course details by key.
     * Endpoint: /courses/{key}
     * Method: GET
     * Permissions: Public or authenticated
     */
    @GET("courses/{key}")
    suspend fun getCourseDetails(
        @Path("key") courseKey: String
    ): Response<Course> // Assuming it returns a single Course object
}
