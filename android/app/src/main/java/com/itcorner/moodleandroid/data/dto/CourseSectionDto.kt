package com.itcorner.moodleandroid.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CourseSectionDto(
    val id: Int,
    val name: String? = null,
    val section: Int? = null
)
