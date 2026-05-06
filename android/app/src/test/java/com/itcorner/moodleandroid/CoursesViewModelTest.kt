package com.itcorner.moodleandroid

import com.itcorner.moodleandroid.domain.Course
import com.itcorner.moodleandroid.domain.ViewState
import com.itcorner.moodleandroid.presentation.courses.CoursesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoursesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadCourses_successState() = runTest {
        val vm = CoursesViewModel(
            FakeCourseRepository(
                coursesResult = Result.success(
                    listOf(Course(id = 1, name = "Physics", progress = 50, imageUrl = null))
                )
            )
        )

        advanceUntilIdle()

        assertTrue(vm.state.value is ViewState.Success)
    }

    @Test
    fun loadCourses_emptyState() = runTest {
        val vm = CoursesViewModel(FakeCourseRepository(coursesResult = Result.success(emptyList())))

        advanceUntilIdle()

        assertTrue(vm.state.value is ViewState.Empty)
    }

    @Test
    fun loadCourses_errorThenRetry_success() = runTest {
        val repo = FakeCourseRepository(coursesResult = Result.failure(IllegalStateException("boom")))
        val vm = CoursesViewModel(repo)

        advanceUntilIdle()
        assertTrue(vm.state.value is ViewState.Error)

        repo.setCoursesResult(
            Result.success(listOf(Course(id = 2, name = "Biology", progress = null, imageUrl = null)))
        )
        vm.retry()
        advanceUntilIdle()

        assertTrue(vm.state.value is ViewState.Success)
    }
}
