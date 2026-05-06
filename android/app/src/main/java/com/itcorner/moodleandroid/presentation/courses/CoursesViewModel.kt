package com.itcorner.moodleandroid.presentation.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcorner.moodleandroid.domain.Course
import com.itcorner.moodleandroid.domain.CourseRepository
import com.itcorner.moodleandroid.domain.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoursesViewModel(
    private val repository: CourseRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<List<Course>>>(ViewState.Idle)
    val state: StateFlow<ViewState<List<Course>>> = _state.asStateFlow()

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            _state.value = ViewState.Loading
            runCatching { repository.getCourses() }
                .onSuccess { courses ->
                    _state.value = if (courses.isEmpty()) {
                        ViewState.Empty("No enrolled courses found.")
                    } else {
                        ViewState.Success(courses)
                    }
                }
                .onFailure { error ->
                    _state.value = ViewState.Error(error.toUserMessage())
                }
        }
    }

    fun retry() = loadCourses()
    fun refresh() = loadCourses()
}

internal fun Throwable.toUserMessage(): String {
    val fallback = "Unable to load data from Moodle right now. Please retry."
    val source = message?.trim().orEmpty()
    if (source.contains("502") || source.contains("503") || source.contains("504")) {
        return "Moodle server is temporarily unavailable. Please retry in a few seconds."
    }
    return source.takeIf { it.isNotBlank() } ?: fallback
}
