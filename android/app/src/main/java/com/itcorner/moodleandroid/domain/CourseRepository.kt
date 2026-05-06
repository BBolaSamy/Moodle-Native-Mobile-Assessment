package com.itcorner.moodleandroid.domain

interface CourseRepository {
    suspend fun getCourses(): List<Course>
    suspend fun getCourseSections(courseId: Int): List<CourseSection>
    suspend fun getGrades(courseId: Int): List<GradeItem>
}
