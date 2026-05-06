package com.itcorner.moodleandroid

import android.app.Application

class MoodleApplication : Application() {
    val container: AppContainer by lazy { AppContainer() }
}
