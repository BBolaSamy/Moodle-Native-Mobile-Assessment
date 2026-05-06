package com.itcorner.moodleandroid

import com.itcorner.moodleandroid.domain.CourseSection
import com.itcorner.moodleandroid.domain.ViewState
import com.itcorner.moodleandroid.presentation.coursedetails.CourseDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CourseDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadSections_successState() = runTest {
        val vm = CourseDetailsViewModel(
            FakeCourseRepository(
                sectionsResult = Result.success(listOf(CourseSection(id = 1, title = "Week 1")))
            )
        )

        vm.load(10)
        advanceUntilIdle()

        assertTrue(vm.state.value is ViewState.Success)
    }

    @Test
    fun loadSections_emptyState() = runTest {
        val vm = CourseDetailsViewModel(
            FakeCourseRepository(sectionsResult = Result.success(emptyList()))
        )

        vm.load(10)
        advanceUntilIdle()

        assertTrue(vm.state.value is ViewState.Empty)
    }

    @Test
    fun loadSections_errorThenRetry_success() = runTest {
        val repo = FakeCourseRepository(
            sectionsResult = Result.failure(IllegalStateException("failed"))
        )
        val vm = CourseDetailsViewModel(repo)

        vm.load(10)
        advanceUntilIdle()
        assertTrue(vm.state.value is ViewState.Error)

        repo.setSectionsResult(Result.success(listOf(CourseSection(id = 2, title = "Week 2"))))
        vm.retry()
        advanceUntilIdle()

        assertTrue(vm.state.value is ViewState.Success)
    }
}
