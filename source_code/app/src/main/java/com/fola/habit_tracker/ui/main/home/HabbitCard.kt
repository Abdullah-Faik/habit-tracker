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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.data.database.RepeatedType
import com.fola.habit_tracker.ui.components.icons.PlusSmall
import com.fola.habit_tracker.ui.components.icons.checked
import com.fola.habit_tracker.ui.components.icons.minus
import com.fola.habit_tracker.ui.components.interFont
import com.fola.habit_tracker.ui.theme.AppTheme
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun TasksCard(
    modifier: Modifier = Modifier,
    habit: Habit,
    onClickable: () -> Unit = {},
    progress: Flow<Float>,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDone: () -> Unit,
) {
    val prog = progress.collectAsState(0f).value
    val _progress = (prog * habit.timesOfUnit).roundToInt()

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(habit.icon),
                        contentDescription = habit.iconDescription,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(20))
                            .background(Color.White)
                            .padding(4.dp)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.padding(horizontal = 8.dp))
                    Column {
                        Text(
                            text = habit.title,
                            fontSize = 22.sp,
                            fontFamily = interFont,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(Modifier.padding(vertical = 4.dp))
                        Text("$_progress ${habit.unit} of ${habit.timesOfUnit} done")

                    }
                }

                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { prog },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        modifier = Modifier
                            .size(52.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50)),
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HabitActionButton(
                    modifier
                        .clip(RoundedCornerShape(35)),
                    onClickable = onDecrease,
                    Color.Red,
                    minus,
                    ""
                )
                HabitActionButton(
                    modifier
                        .clip(RoundedCornerShape(35)),
                    onClickable = onIncrease,
                    Color.Cyan,
                    PlusSmall,
                    ""
                )
                HabitActionButton(
                    modifier
                        .clip(RoundedCornerShape(35)),
                    onClickable = onDone,
                    Color.Green,
                    checked,
                    ""
                )
            }

            Row(
                modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(35))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    "",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.padding(horizontal = 2.dp))
                Text(
                    "${habit.repeatedType.name.lowercase().capitalize()} at ${
                        habit.startTime.format(
                            DateTimeFormatter.ofPattern("hh:mm a")
                        )
                    }"
                )
            }
        }
    }
}


private fun test() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(16, 16, 0, 0))
//            .heightIn(min = 54.dp, max = 72.dp)
//            .clickable { onClickable() }
//            .background(SolidColor(Color(habit.color)), alpha = 0.4f)
//            .padding(horizontal = 4.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//
//    ) {
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .padding(vertical = 8.dp)
//                .weight(.8f),
//        ) {
//            Image(
//                painter = painterResource(habit.icon),
//                contentDescription = habit.iconDescription,
//                modifier = Modifier
//                    .padding(8.dp)
//                    .weight(.2f)
//                    .clip(RoundedCornerShape(12))
//                    .aspectRatio(1f)
//                    //.border((2).dp, Color.White, RoundedCornerShape(20))
//                    .background(Color.White)
//                    .padding(4.dp),
//
//                contentScale = ContentScale.Crop
//            )
//            Column(
//                modifier = Modifier
//                    .weight(.6f)
//            ) {
//                Text(
//                    text = habit.title,
//                    color = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier
//                        .padding(bottom = 2.dp)
//                )
//                Row(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(20))
//                        .background(MaterialTheme.colorScheme.onSecondary)
//                        .padding(horizontal = 4.dp),
//                    horizontalArrangement = Arrangement.spacedBy(4.dp),
//                    verticalAlignment = Alignment.CenterVertically
//
//                ) {
//                    Text(
//                        text = if (habit.repeatedType == RepeatedType.ONCE) "Task" else "Habit",
//                        fontSize = 10.sp,
//                        color = MaterialTheme.colorScheme.onSurface,
//                    )
//                    Text(
//                        text = "⏰ ${habit.startTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
//                        fontSize = 10.sp,
//                        color = MaterialTheme.colorScheme.onSurface,
//                    )
//                }
//            }
//        }
//        Box(
//            modifier = Modifier
//                .weight(.15f)
//                .fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator(
//                progress = { progress },
//                color = MaterialTheme.colorScheme.primary,
//                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
//                modifier = Modifier
//                    .size(60.dp)
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(50))
//                    .background(
//                        brush = SolidColor(Color(0xB766E36A)),
//                        alpha = progress.coerceAtLeast(0.5f),
//                        shape = RoundedCornerShape(50)
//                    )
//                    .fillMaxSize(),
//                strokeWidth = 5.dp
//            )
//        }
//    }
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(0, 0, 16, 16))
//            .background(MaterialTheme.colorScheme.secondaryContainer)
//            .height(36.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//
//    ) {
//        HabitActionButton(
//            modifier
//                .weight(.2f)
//                .padding(end = 24.dp)
//                .clip(RoundedCornerShape(0, 50, 50, 0)),
//            onClickable = onDecrease,
//            Color.Red,
//            minus,
//            ""
//        )
//        HabitActionButton(
//            modifier
//                .weight(.2f)
//                .padding(horizontal = 12.dp)
//                .clip(RoundedCornerShape(50)),
//            onClickable = onIncrease,
//            Color.Cyan,
//            PlusSmall,
//            ""
//        )
//        HabitActionButton(
//            modifier
//                .weight(.2f)
//                .padding(start = 24.dp)
//                .clip(RoundedCornerShape(50, 0, 0, 50)),
//            onClickable = onDone,
//            Color.Green,
//            checked,
//            ""
//        )
//
//    }
//}}
}


@Composable
fun HabitActionButton(
    modifier: Modifier = Modifier,
    onClickable: () -> Unit = {},
    backGroundColor: Color = Color.Transparent,
    icon: ImageVector,
    contentDescription: String?,
    isActive: Boolean = true

) {
    IconButton(
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
    title = "Test progress",
    unit = "glass",
    timesOfUnit = 7
)

@Preview(showBackground = true)
@Composable
private fun TasksCardLightPrev() {
    AppTheme {
        TasksCard(
            habit = habit,
            modifier = Modifier,
            onClickable = { },
            progress = flow { .3f },
            onDone = {},
            onDecrease = {},
            onIncrease = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun TasksCardDarkPrev(modifier: Modifier = Modifier) {
    AppTheme {
        TasksCard(
            habit = habit,
            modifier = Modifier,
            onClickable = { },
            progress = flow { .2f },
            onDone = {},
            onDecrease = {},
            onIncrease = {}
        )
    }
}