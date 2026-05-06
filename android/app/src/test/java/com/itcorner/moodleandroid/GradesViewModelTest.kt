package com.itcorner.moodleandroid

import com.itcorner.moodleandroid.domain.GradeItem
import com.itcorner.moodleandroid.domain.ViewState
import com.itcorner.moodleandroid.presentation.grades.GradesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GradesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadGrades_successState() = runTest {
        val vm = GradesViewModel(
            FakeCourseRepository(
                gradesResult = Result.success(
                    listOf(GradeItem(id = 1, itemName = "Quiz", gradeText = "18/20", percentageText = "90%"))
                )
            )
        )

        vm.load(10)
        advanceUntilIdle()

        assertTrue(vm.state.value is ViewState.Success)
    }

    @Test
    fun loadGrades_emptyState() = runTest {
        val vm = GradesViewModel(
            FakeCourseRepository(gradesResult = Result.success(emptyList()))
        )

        vm.load(10)
        advanceUntilIdle()

        assertTrue(vm.state.value is ViewState.Empty)
    }

    @Test
    fun loadGrades_errorThenRetry_success() = runTest {
        val repo = FakeCourseRepository(
            gradesResult = Result.failure(IllegalStateException("failed"))
        )
        val vm = GradesViewModel(repo)

        vm.load(10)
        advanceUntilIdle()
        assertTrue(vm.state.value is ViewState.Error)

        repo.setGradesResult(
            Result.success(listOf(GradeItem(id = 2, itemName = "Assignment", gradeText = "A", percentageText = "95%")))
        )
        vm.retry()
        advanceUntilIdle()

        assertTrue(vm.state.value is ViewState.Success)
    }
}
