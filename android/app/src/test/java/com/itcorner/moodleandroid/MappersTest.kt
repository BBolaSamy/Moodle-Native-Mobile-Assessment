package com.itcorner.moodleandroid

import com.itcorner.moodleandroid.data.dto.CourseDto
import com.itcorner.moodleandroid.data.dto.GradeItemDto
import com.itcorner.moodleandroid.data.dto.OverviewFileDto
import com.itcorner.moodleandroid.data.mappers.CourseMapper
import com.itcorner.moodleandroid.data.mappers.GradeMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MappersTest {

    @Test
    fun courseMapper_usesCourseImageFallback() {
        val dto = CourseDto(
            id = 4,
            fullname = "AI 101",
            displayname = "AI101 Intro to AI",
            progress = null,
            completionprogress = 75,
            courseimage = "https://example.com/course.svg",
            overviewfiles = emptyList()
        )

        val model = CourseMapper.map(dto)

        assertEquals("AI101 Intro to AI", model.name)
        assertEquals(75, model.progress)
        assertEquals("https://example.com/course.svg", model.imageUrl)
    }

    @Test
    fun courseMapper_usesOverviewFileWhenCourseImageMissing() {
        val dto = CourseDto(
            id = 10,
            fullname = "Math",
            displayname = null,
            progress = 20,
            completionprogress = null,
            courseimage = null,
            overviewfiles = listOf(OverviewFileDto(fileurl = "https://example.com/cover.png"))
        )

        val model = CourseMapper.map(dto)

        assertEquals("https://example.com/cover.png", model.imageUrl)
        assertEquals("Math", model.name)
    }

    @Test
    fun gradeMapper_treatsDashAsNotGraded() {
        val dto = GradeItemDto(
            id = 11,
            itemname = "Assignment 1",
            gradeformatted = "-",
            percentageformatted = "-",
            graderaw = null,
            grademin = 0.0,
            grademax = 100.0
        )

        val model = GradeMapper.map(dto)

        assertEquals("Assignment 1", model.itemName)
        assertEquals("Not graded", model.gradeText)
        assertNull(model.percentageText)
    }
}
