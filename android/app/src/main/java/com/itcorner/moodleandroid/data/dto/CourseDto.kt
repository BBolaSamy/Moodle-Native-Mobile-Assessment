package com.itcorner.moodleandroid.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CourseDto(
    val id: Int,
    val fullname: String? = null,
    val displayname: String? = null,
    val progress: Int? = null,
    val completionprogress: Int? = null,
    val courseimage: String? = null,
    val overviewfiles: List<OverviewFileDto>? = null
)

@JsonClass(generateAdapter = true)
data class OverviewFileDto(
    val fileurl: String? = null
)
