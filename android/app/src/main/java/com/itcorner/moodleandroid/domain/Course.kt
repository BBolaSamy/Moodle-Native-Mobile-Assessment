package com.itcorner.moodleandroid.domain

data class Course(
    val id: Int,
    val name: String,
    val progress: Int?,
    val imageUrl: String?
)
