package com.itcorner.moodleandroid.presentation.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.itcorner.moodleandroid.domain.Course
import com.itcorner.moodleandroid.domain.ViewState
import com.itcorner.moodleandroid.presentation.common.LoadingView
import com.itcorner.moodleandroid.presentation.common.MessageView

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CoursesScreen(
    state: ViewState<List<Course>>,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onOpenCourse: (Course) -> Unit,
    onOpenGrades: (Course) -> Unit
) {
    val isRefreshing = state is ViewState.Loading
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .pullRefresh(pullRefreshState)
    ) {
        when (state) {
            is ViewState.Idle, is ViewState.Loading -> LoadingView()
            is ViewState.Empty -> MessageView(message = state.message, actionLabel = "Retry", onAction = onRetry)
            is ViewState.Error -> MessageView(message = state.message, actionLabel = "Retry", onAction = onRetry)
            is ViewState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.data, key = { it.id }) { course ->
                        CourseRow(
                            course = course,
                            onOpenCourse = { onOpenCourse(course) },
                            onOpenGrades = { onOpenGrades(course) }
                        )
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun CourseRow(
    course: Course,
    onOpenCourse: () -> Unit,
    onOpenGrades: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenCourse),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!course.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = course.imageUrl,
                    contentDescription = course.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = course.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = course.progress?.let { "Progress: $it%" } ?: "Progress: N/A",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            OutlinedButton(onClick = onOpenGrades) {
                Text("Grades")
            }
        }
    }
}
