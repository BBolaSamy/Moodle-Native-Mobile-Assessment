package com.itcorner.moodleandroid.presentation.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itcorner.moodleandroid.domain.CourseRepository
import com.itcorner.moodleandroid.domain.GradeItem
import com.itcorner.moodleandroid.domain.ViewState
import com.itcorner.moodleandroid.presentation.courses.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GradesViewModel(
    private val repository: CourseRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<List<GradeItem>>>(ViewState.Idle)
    val state: StateFlow<ViewState<List<GradeItem>>> = _state.asStateFlow()

    private var currentCourseId: Int? = null

    fun load(courseId: Int) {
        currentCourseId = courseId
        viewModelScope.launch {
            _state.value = ViewState.Loading
            runCatching { repository.getGrades(courseId) }
                .onSuccess { grades ->
                    _state.value = if (grades.isEmpty()) {
                        ViewState.Empty("No grades published yet.")
                    } else {
                        ViewState.Success(grades)
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
