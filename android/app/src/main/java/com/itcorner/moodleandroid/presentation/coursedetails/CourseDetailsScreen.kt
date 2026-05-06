package com.itcorner.moodleandroid.presentation.coursedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itcorner.moodleandroid.domain.CourseSection
import com.itcorner.moodleandroid.domain.ViewState
import com.itcorner.moodleandroid.presentation.common.LoadingView
import com.itcorner.moodleandroid.presentation.common.MessageView

@Composable
fun CourseDetailsScreen(
    state: ViewState<List<CourseSection>>,
    onRetry: () -> Unit
) {
    when (state) {
        is ViewState.Idle, is ViewState.Loading -> LoadingView()
        is ViewState.Empty -> MessageView(message = state.message, actionLabel = "Retry", onAction = onRetry)
        is ViewState.Error -> MessageView(message = state.message, actionLabel = "Retry", onAction = onRetry)
        is ViewState.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.data, key = { it.id }) { section ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = section.title,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
