package com.itcorner.moodleandroid.data.network

enum class MoodleFunction(val raw: String) {
    COURSES("core_enrol_get_users_courses"),
    COURSE_CONTENTS("core_course_get_contents"),
    GRADES("gradereport_user_get_grade_items")
}
