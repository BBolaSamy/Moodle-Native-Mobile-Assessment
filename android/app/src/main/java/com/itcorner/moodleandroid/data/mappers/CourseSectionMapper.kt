package com.itcorner.moodleandroid.data.mappers

import com.itcorner.moodleandroid.data.dto.CourseSectionDto
import com.itcorner.moodleandroid.domain.CourseSection

object CourseSectionMapper {
    fun map(dto: CourseSectionDto): CourseSection {
        val sectionNumber = dto.section ?: 0
        val title = dto.name?.takeIf { it.isNotBlank() } ?: "Section $sectionNumber"
        return CourseSection(id = dto.id, title = title)
    }
}
