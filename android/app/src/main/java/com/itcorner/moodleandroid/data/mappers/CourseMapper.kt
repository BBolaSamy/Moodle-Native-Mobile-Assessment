package com.itcorner.moodleandroid.data.mappers

import com.itcorner.moodleandroid.data.dto.CourseDto
import com.itcorner.moodleandroid.domain.Course

object CourseMapper {
    fun map(dto: CourseDto): Course {
        val preferredName = dto.displayname?.takeIf { it.isNotBlank() }
            ?: dto.fullname?.takeIf { it.isNotBlank() }
            ?: "Untitled course"

        val resolvedImage = dto.courseimage?.takeIf { it.isNotBlank() }
            ?: dto.overviewfiles?.firstOrNull { !it.fileurl.isNullOrBlank() }?.fileurl

        return Course(
            id = dto.id,
            name = preferredName,
            progress = dto.progress ?: dto.completionprogress,
            imageUrl = resolvedImage
        )
    }
}
