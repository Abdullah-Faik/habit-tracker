package com.fola.habit_tracker.ui.main.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fola.habit_tracker.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.flow

@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    progress: Flow<Float>,
    allTasks : Int = 0,
    completedTask : Flow<Int>
) {
    val _progress by progress.collectAsState(0f)
    val _completed by completedTask.collectAsState(0f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .shadow(8.dp)
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator(
                progress = {
                    _progress
                },
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .size(72.dp),
                strokeWidth = 6.dp,
                strokeCap = StrokeCap.Round
            )
            Text(
                text = "${(_progress * 100).toInt()}%",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Column {
            Text(
                "You Are On Fire  \uD83D\uDD25",
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "$_completed of $allTasks Completed",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun ProgressCardView() {
    AppTheme {
        ProgressCard(
            modifier = TODO(),
            progress = TODO(),
            allTasks = TODO(),
            completedTask = TODO()
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProgressCardDarkView() {
    AppTheme {
        ProgressCard(
            modifier = TODO(),
            progress = TODO(),
            allTasks = TODO(),
            completedTask = TODO()
        )
    }
}