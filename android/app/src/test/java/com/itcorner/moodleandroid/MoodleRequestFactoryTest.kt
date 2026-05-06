package com.itcorner.moodleandroid

import com.itcorner.moodleandroid.data.network.MoodleFunction
import com.itcorner.moodleandroid.data.network.MoodleRequestFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class MoodleRequestFactoryTest {

    private val factory = MoodleRequestFactory(
        baseUrl = "https://moodle.itcorner.qzz.io/webservice/rest/server.php",
        token = "abc-token"
    )

    @Test
    fun userCoursesRequest_containsRequiredQueryItems() {
        val url = factory.createUrl(
            function = MoodleFunction.COURSES,
            additional = mapOf("userid" to "1003")
        )

        assertEquals("abc-token", url.queryParameter("wstoken"))
        assertEquals("core_enrol_get_users_courses", url.queryParameter("wsfunction"))
        assertEquals("json", url.queryParameter("moodlewsrestformat"))
        assertEquals("1003", url.queryParameter("userid"))
    }

    @Test
    fun courseContentsRequest_containsCourseId() {
        val url = factory.createUrl(
            function = MoodleFunction.COURSE_CONTENTS,
            additional = mapOf("courseid" to "4")
        )

        assertEquals("core_course_get_contents", url.queryParameter("wsfunction"))
        assertEquals("4", url.queryParameter("courseid"))
    }

    @Test
    fun gradesRequest_containsCourseAndUser() {
        val url = factory.createUrl(
            function = MoodleFunction.GRADES,
            additional = mapOf("userid" to "1003", "courseid" to "4")
        )

        assertEquals("gradereport_user_get_grade_items", url.queryParameter("wsfunction"))
        assertEquals("1003", url.queryParameter("userid"))
        assertEquals("4", url.queryParameter("courseid"))
    }
}
