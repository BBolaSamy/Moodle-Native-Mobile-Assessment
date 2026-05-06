package com.itcorner.moodleandroid.presentation.grades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.itcorner.moodleandroid.domain.GradeItem
import com.itcorner.moodleandroid.domain.ViewState
import com.itcorner.moodleandroid.presentation.common.LoadingView
import com.itcorner.moodleandroid.presentation.common.MessageView

@Composable
fun GradesScreen(
    state: ViewState<List<GradeItem>>,
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
                items(state.data, key = { it.id }) { grade ->
                    GradeRow(grade)
                }
            }
        }
    }
}

@Composable
private fun GradeRow(gradeItem: GradeItem) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Text(text = gradeItem.itemName, style = MaterialTheme.typography.titleMedium)
            Text(
                text = gradeItem.gradeText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 6.dp)
            )
            gradeItem.percentageText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
