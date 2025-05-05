package com.fola.habit_tracker.ui.main.home

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R
import com.fola.habit_tracker.data.data_base.Habit
import com.fola.habit_tracker.data.data_base.RepeatedType
import com.fola.habit_tracker.ui.theme.AppTheme
import java.time.format.DateTimeFormatter

@Composable
fun TasksCard(
    modifier: Modifier = Modifier,
    habit: Habit,
    onClickable: () -> Unit = {},
    progress: Float = .4f
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .heightIn(min = 72.dp, max = 96.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 8.dp)
                .clickable { onClickable() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(.8f),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier
                        .weight(.2f)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(20))
                        .aspectRatio(1f)
                        .border((2).dp, Color.White, RoundedCornerShape(20)),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .weight(.6f)
                ) {
                    Text(
                        text = habit.title,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(
                            text = if (habit.repeatedType == RepeatedType.ONCE) "Task" else "Habit",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "⏰ ${habit.startTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(.15f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    modifier = Modifier
                        .size(60.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50))
                        .background(
                            brush = SolidColor(Color(0xB766E36A)),
                            alpha = progress,
                            shape = RoundedCornerShape(50)
                        )
                        .fillMaxSize(),
                    strokeWidth = 5.dp
                )
            }
        }
        Row {

        }
    }

}


@Composable
fun HabitActionButton(
    modifier: Modifier = Modifier,
    onClickable: () -> Unit,
    backGroundColor: Color,
    @DrawableRes icon: Int,
    contentDescription: String?,

) {
    Row {

    }

}

private val habit = Habit(
    title = "Test",
)

@Preview
@Composable
private fun TasksCardLightPrev() {
    AppTheme {
        TasksCard(habit = habit)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TasksCardDarkPrev(modifier: Modifier = Modifier) {
    AppTheme {
        TasksCard(habit = habit)
    }
}