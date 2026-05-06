package com.itcorner.moodleandroid.data.mappers

import com.itcorner.moodleandroid.data.dto.GradeItemDto
import com.itcorner.moodleandroid.domain.GradeItem

object GradeMapper {
    fun map(dto: GradeItemDto): GradeItem {
        val normalizedGrade = dto.gradeformatted
            ?.trim()
            ?.takeIf { it.isNotEmpty() && it != "-" }
            ?: "Not graded"

        val normalizedPercentage = dto.percentageformatted
            ?.trim()
            ?.takeIf { it.isNotEmpty() && it != "-" }

        return GradeItem(
            id = dto.id,
            itemName = dto.itemname?.takeIf { it.isNotBlank() } ?: "Unnamed item",
            gradeText = normalizedGrade,
            percentageText = normalizedPercentage
        )
    }
}
