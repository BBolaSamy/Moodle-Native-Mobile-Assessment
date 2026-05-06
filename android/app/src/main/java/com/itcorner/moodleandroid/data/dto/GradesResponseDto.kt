package com.itcorner.moodleandroid.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GradesResponseDto(
    val usergrades: List<UserGradeDto>? = null
)

@JsonClass(generateAdapter = true)
data class UserGradeDto(
    val courseid: Int? = null,
    val gradeitems: List<GradeItemDto>? = null
)

@JsonClass(generateAdapter = true)
data class GradeItemDto(
    val id: Int,
    val itemname: String? = null,
    val gradeformatted: String? = null,
    val percentageformatted: String? = null,
    val graderaw: Double? = null,
    val grademin: Double? = null,
    val grademax: Double? = null
)
