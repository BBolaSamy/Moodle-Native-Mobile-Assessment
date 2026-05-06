package com.itcorner.moodleandroid.data.network

import com.itcorner.moodleandroid.data.dto.CourseDto
import com.itcorner.moodleandroid.data.dto.CourseSectionDto
import com.itcorner.moodleandroid.data.dto.GradesResponseDto
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MoodleApiService {
    @GET("server.php")
    suspend fun getUserCourses(
        @QueryMap(encoded = true) params: Map<String, String>
    ): List<CourseDto>

    @GET("server.php")
    suspend fun getCourseContents(
        @QueryMap(encoded = true) params: Map<String, String>
    ): List<CourseSectionDto>

    @GET("server.php")
    suspend fun getGradeItems(
        @QueryMap(encoded = true) params: Map<String, String>
    ): GradesResponseDto
}
