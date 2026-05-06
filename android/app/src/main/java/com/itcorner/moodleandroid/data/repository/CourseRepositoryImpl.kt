package com.itcorner.moodleandroid.data.repository

import com.itcorner.moodleandroid.data.mappers.CourseMapper
import com.itcorner.moodleandroid.data.mappers.CourseSectionMapper
import com.itcorner.moodleandroid.data.mappers.GradeMapper
import com.itcorner.moodleandroid.data.network.MoodleApiService
import com.itcorner.moodleandroid.data.network.MoodleConfig
import com.itcorner.moodleandroid.data.network.MoodleFunction
import com.itcorner.moodleandroid.data.network.MoodleRequestFactory
import com.itcorner.moodleandroid.domain.Course
import com.itcorner.moodleandroid.domain.CourseRepository
import com.itcorner.moodleandroid.domain.CourseSection
import com.itcorner.moodleandroid.domain.GradeItem
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class CourseRepositoryImpl(
    private val api: MoodleApiService,
    private val requestFactory: MoodleRequestFactory
) : CourseRepository {

    override suspend fun getCourses(): List<Course> {
        val params = requestFactory.createQueryMap(
            function = MoodleFunction.COURSES,
            additional = mapOf("userid" to MoodleConfig.USER_ID.toString())
        )

        return withRetry {
            api.getUserCourses(params)
        }.map(CourseMapper::map)
    }

    override suspend fun getCourseSections(courseId: Int): List<CourseSection> {
        val params = requestFactory.createQueryMap(
            function = MoodleFunction.COURSE_CONTENTS,
            additional = mapOf("courseid" to courseId.toString())
        )

        return withRetry {
            api.getCourseContents(params)
        }.map(CourseSectionMapper::map)
    }

    override suspend fun getGrades(courseId: Int): List<GradeItem> {
        val params = requestFactory.createQueryMap(
            function = MoodleFunction.GRADES,
            additional = mapOf(
                "userid" to MoodleConfig.USER_ID.toString(),
                "courseid" to courseId.toString()
            )
        )

        val grades = withRetry {
            api.getGradeItems(params)
        }

        val userGrade = grades.usergrades?.firstOrNull()
        return userGrade?.gradeitems.orEmpty().map(GradeMapper::map)
    }

    private suspend fun <T> withRetry(maxAttempts: Int = 3, block: suspend () -> T): T {
        var attempt = 0
        var delayMs = 600L
        var lastError: Throwable? = null

        while (attempt < maxAttempts) {
            try {
                return block()
            } catch (error: Throwable) {
                lastError = error
                val retryable = isRetryable(error)
                attempt++
                if (!retryable || attempt >= maxAttempts) break
                delay(delayMs)
                delayMs *= 2
            }
        }

        throw lastError ?: IllegalStateException("Unknown error")
    }

    private fun isRetryable(error: Throwable): Boolean {
        return when (error) {
            is IOException -> true
            is HttpException -> error.code() in 500..599
            else -> false
        }
    }
}
