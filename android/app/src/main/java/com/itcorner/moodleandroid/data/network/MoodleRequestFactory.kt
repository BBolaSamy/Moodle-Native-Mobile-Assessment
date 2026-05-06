package com.itcorner.moodleandroid.data.network

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

class MoodleRequestFactory(
    baseUrl: String,
    private val token: String
) {
    private val endpoint = baseUrl.toHttpUrl()

    fun createUrl(function: MoodleFunction, additional: Map<String, String> = emptyMap()): HttpUrl {
        val builder = endpoint.newBuilder()
            .addQueryParameter("wstoken", token)
            .addQueryParameter("wsfunction", function.raw)
            .addQueryParameter("moodlewsrestformat", MoodleConfig.REST_FORMAT)

        additional.forEach { (key, value) -> builder.addQueryParameter(key, value) }
        return builder.build()
    }

    fun createQueryMap(function: MoodleFunction, additional: Map<String, String> = emptyMap()): Map<String, String> {
        return buildMap {
            put("wstoken", token)
            put("wsfunction", function.raw)
            put("moodlewsrestformat", MoodleConfig.REST_FORMAT)
            putAll(additional)
        }
    }
}
