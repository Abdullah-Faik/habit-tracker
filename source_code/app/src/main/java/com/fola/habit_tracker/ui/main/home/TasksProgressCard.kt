package com.fola.habit_tracker.ui.main.home

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
import kotlinx.coroutines.flow.flow

@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    progress : Flow<Float>
) {

    val _progress = progress.collectAsState(initial = 0f)

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
                    _progress.value
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
                text = "${(_progress.value * 100).toInt()}%",
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
                "1 of 5 Completed",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun ProgressCardView() {
    AppTheme {
        ProgressCard(progress = flow { emit(0f) })
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProgressCardDarkView() {
    AppTheme {
        ProgressCard(progress = flow { emit(0f) })
    }
}