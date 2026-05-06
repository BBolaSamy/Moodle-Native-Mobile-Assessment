package com.itcorner.moodleandroid

import com.itcorner.moodleandroid.domain.Course
import com.itcorner.moodleandroid.domain.CourseRepository
import com.itcorner.moodleandroid.domain.CourseSection
import com.itcorner.moodleandroid.domain.GradeItem

class FakeCourseRepository(
    private var coursesResult: Result<List<Course>> = Result.success(emptyList()),
    private var sectionsResult: Result<List<CourseSection>> = Result.success(emptyList()),
    private var gradesResult: Result<List<GradeItem>> = Result.success(emptyList())
) : CourseRepository {

    override suspend fun getCourses(): List<Course> = coursesResult.getOrThrow()

    override suspend fun getCourseSections(courseId: Int): List<CourseSection> = sectionsResult.getOrThrow()

    override suspend fun getGrades(courseId: Int): List<GradeItem> = gradesResult.getOrThrow()

    fun setCoursesResult(value: Result<List<Course>>) {
        coursesResult = value
    }

    fun setSectionsResult(value: Result<List<CourseSection>>) {
        sectionsResult = value
    }

    fun setGradesResult(value: Result<List<GradeItem>>) {
        gradesResult = value
    }
}
