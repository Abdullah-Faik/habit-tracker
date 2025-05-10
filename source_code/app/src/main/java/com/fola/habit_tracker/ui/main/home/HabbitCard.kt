package com.fola.habit_tracker.ui.main.home

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.data.database.RepeatedType
import com.fola.habit_tracker.ui.components.icons.PlusSmall
import com.fola.habit_tracker.ui.components.icons.checked
import com.fola.habit_tracker.ui.components.icons.minus
import com.fola.habit_tracker.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import java.time.format.DateTimeFormatter

@Composable
fun TasksCard(
    modifier: Modifier = Modifier,
    habit: Habit,
    onClickable: () -> Unit = {},
    progress: Float = .4f,
    onIncrease :() -> Unit,
    onDecrease : () -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16, 16, 0, 0))
                .heightIn(min = 54.dp, max = 72.dp)
                .clickable { onClickable() }
                .background(SolidColor(Color(habit.color)), alpha = 0.4f)
                .padding(horizontal = 4.dp),
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
                    painter = painterResource(habit.icon),
                    contentDescription = habit.iconDescription,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(.2f)
                        .clip(RoundedCornerShape(12))
                        .aspectRatio(1f)
                        //.border((2).dp, Color.White, RoundedCornerShape(20))
                        .background(Color.White)
                        .padding(4.dp),

                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .weight(.6f)
                ) {
                    Text(
                        text = habit.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(bottom = 2.dp)
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
                            text = if (habit.repeatedType == RepeatedType.TASK) "Task" else "Habit",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "⏰ ${habit.reminderTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
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
                            alpha = progress.coerceAtLeast(0.5f),
                            shape = RoundedCornerShape(50)
                        )
                        .fillMaxSize(),
                    strokeWidth = 5.dp
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(0, 0, 16, 16))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .height(36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            HabitActionButton(
                modifier
                    .weight(.2f)
                    .padding(end = 24.dp)
                    .clip(RoundedCornerShape(0, 50, 50, 0))
                ,
                onClickable = onDecrease,
                Color.Red,
                minus,
                ""
            )
            HabitActionButton(
                modifier
                    .weight(.2f)
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(50))
                ,
                onClickable = onIncrease,
                Color.Cyan,
                PlusSmall,
                ""
            )
            HabitActionButton(
                modifier
                    .weight(.2f)
                    .padding(start = 24.dp)
                    .clip(RoundedCornerShape(50, 0, 0, 50))
                ,
                onClickable = onDone,
                Color.Green,
                checked,
                ""
            )

        }
    }

}


@Composable
fun HabitActionButton(
    modifier: Modifier = Modifier,
    onClickable: () -> Unit = {},
    backGroundColor: Color = Color.Transparent,
    icon: ImageVector,
    contentDescription: String?,
    isActive : Boolean = true

    ) {
    IconButton (
        onClick = { onClickable() },
        modifier = modifier
            .background(SolidColor(backGroundColor), alpha = .4f),
        enabled = isActive
    ) {
        Icon(
            icon, contentDescription, modifier = Modifier
                .clip(RoundedCornerShape(25))
        )
    }


}

private val habit = Habit(
    title = "Test",
)

@Preview(showBackground = true)
@Composable
private fun TasksCardLightPrev() {
    AppTheme {
        TasksCard(
            habit = habit,
            modifier = Modifier,
            onClickable = {  },
            onDone = {},
            onDecrease = {},
            onIncrease = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES , showBackground = true)
@Composable
fun TasksCardDarkPrev(modifier: Modifier = Modifier) {
    AppTheme {
        TasksCard(
            habit = habit,
            modifier = TODO(),
            onClickable = {  },
            onDone = {},
            onDecrease = {},
            onIncrease = {}
        )
    }
}