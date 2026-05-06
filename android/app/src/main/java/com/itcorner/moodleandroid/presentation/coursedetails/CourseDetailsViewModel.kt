package com.itcorner.moodleandroid.presentation.coursedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcorner.moodleandroid.domain.CourseRepository
import com.itcorner.moodleandroid.domain.CourseSection
import com.itcorner.moodleandroid.domain.ViewState
import com.itcorner.moodleandroid.presentation.courses.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseDetailsViewModel(
    private val repository: CourseRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<List<CourseSection>>>(ViewState.Idle)
    val state: StateFlow<ViewState<List<CourseSection>>> = _state.asStateFlow()

    private var currentCourseId: Int? = null

    fun load(courseId: Int) {
        currentCourseId = courseId
        viewModelScope.launch {
            _state.value = ViewState.Loading
            runCatching { repository.getCourseSections(courseId) }
                .onSuccess { sections ->
                    _state.value = if (sections.isEmpty()) {
                        ViewState.Empty("No sections available for this course.")
                    } else {
                        ViewState.Success(sections)
                    }
                }
                .onFailure { error ->
                    _state.value = ViewState.Error(error.toUserMessage())
                }
        }
    }

    fun retry() {
        currentCourseId?.let(::load)
    }
}
