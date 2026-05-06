package com.itcorner.moodleandroid

import com.itcorner.moodleandroid.data.network.MoodleConfig
import com.itcorner.moodleandroid.data.network.MoodleRequestFactory
import com.itcorner.moodleandroid.data.network.NetworkModule
import com.itcorner.moodleandroid.data.repository.CourseRepositoryImpl
import com.itcorner.moodleandroid.domain.CourseRepository

class AppContainer {
    private val requestFactory = MoodleRequestFactory(
        baseUrl = "${MoodleConfig.BASE_URL}${MoodleConfig.REST_ENDPOINT}",
        token = MoodleConfig.TOKEN
    )

    val courseRepository: CourseRepository = CourseRepositoryImpl(
        api = NetworkModule.apiService,
        requestFactory = requestFactory
    )
}
