package com.itcorner.moodleandroid

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.itcorner.moodleandroid.domain.Course
import com.itcorner.moodleandroid.presentation.common.ViewModelFactory
import com.itcorner.moodleandroid.presentation.coursedetails.CourseDetailsScreen
import com.itcorner.moodleandroid.presentation.coursedetails.CourseDetailsViewModel
import com.itcorner.moodleandroid.presentation.courses.CoursesScreen
import com.itcorner.moodleandroid.presentation.courses.CoursesViewModel
import com.itcorner.moodleandroid.presentation.grades.GradesScreen
import com.itcorner.moodleandroid.presentation.grades.GradesViewModel
import com.itcorner.moodleandroid.ui.theme.MoodleAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as MoodleApplication
        setContent {
            MoodleAndroidTheme {
                MoodleApp(repositoryContainer = app.container)
            }
        }
    }
}

private object Destinations {
    const val COURSES = "courses"
    const val DETAILS = "details/{courseId}/{courseName}"
    const val GRADES = "grades/{courseId}/{courseName}"

    fun detailsRoute(course: Course): String {
        return "details/${course.id}/${Uri.encode(course.name)}"
    }

    fun gradesRoute(course: Course): String {
        return "grades/${course.id}/${Uri.encode(course.name)}"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoodleApp(repositoryContainer: AppContainer) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()

    val title = when (backStack?.destination?.route?.substringBefore('/')) {
        "details" -> backStack?.arguments?.getString("courseName") ?: "Course Details"
        "grades" -> "Grades"
        else -> "Courses"
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) })
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.COURSES,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destinations.COURSES) {
                val vm: CoursesViewModel = viewModel(
                    factory = ViewModelFactory {
                        CoursesViewModel(repositoryContainer.courseRepository)
                    }
                )
                val state by vm.state.collectAsStateWithLifecycle()

                CoursesScreen(
                    state = state,
                    onRefresh = vm::refresh,
                    onRetry = vm::retry,
                    onOpenCourse = { course -> navController.navigate(Destinations.detailsRoute(course)) },
                    onOpenGrades = { course -> navController.navigate(Destinations.gradesRoute(course)) }
                )
            }

            composable(
                route = Destinations.DETAILS,
                arguments = listOf(
                    navArgument("courseId") { type = NavType.IntType },
                    navArgument("courseName") { type = NavType.StringType }
                )
            ) { entry ->
                val courseId = entry.arguments?.getInt("courseId") ?: return@composable
                val vm: CourseDetailsViewModel = viewModel(
                    factory = ViewModelFactory {
                        CourseDetailsViewModel(repositoryContainer.courseRepository)
                    }
                )
                val state by vm.state.collectAsStateWithLifecycle()

                if (state is com.itcorner.moodleandroid.domain.ViewState.Idle) {
                    vm.load(courseId)
                }

                CourseDetailsScreen(
                    state = state,
                    onRetry = vm::retry
                )
            }

            composable(
                route = Destinations.GRADES,
                arguments = listOf(
                    navArgument("courseId") { type = NavType.IntType },
                    navArgument("courseName") { type = NavType.StringType }
                )
            ) { entry ->
                val courseId = entry.arguments?.getInt("courseId") ?: return@composable
                val vm: GradesViewModel = viewModel(
                    factory = ViewModelFactory {
                        GradesViewModel(repositoryContainer.courseRepository)
                    }
                )
                val state by vm.state.collectAsStateWithLifecycle()

                if (state is com.itcorner.moodleandroid.domain.ViewState.Idle) {
                    vm.load(courseId)
                }

                GradesScreen(
                    state = state,
                    onRetry = vm::retry
                )
            }
        }
    }
}
