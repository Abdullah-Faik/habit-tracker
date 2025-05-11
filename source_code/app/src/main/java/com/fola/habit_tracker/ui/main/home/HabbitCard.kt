package com.fola.habit_tracker.ui.main.home

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.ui.components.icons.PlusSmall
import com.fola.habit_tracker.ui.components.icons.checked
import com.fola.habit_tracker.ui.components.icons.minus
import com.fola.habit_tracker.ui.components.interFont
import com.fola.habit_tracker.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun TasksCard(
    modifier: Modifier = Modifier,
    habit: Habit,
    onClickable: () -> Unit = {},
    progress: Flow<Int>,
    onIncrease: (Habit) -> Unit,
    onDecrease: (Habit) -> Unit,
    onDone: (Habit) -> Unit,
) {
    val prog = progress.collectAsState(0).value


    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .background(SolidColor(Color(habit.color)), alpha = 0.2f)
                .padding(vertical = 8.dp),
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
                        Text("$prog ${habit.unit} of ${habit.timesOfUnit} done")

                    }
                }

                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { (prog.toFloat() / habit.timesOfUnit).toFloat() },
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
                    onClickable = { onDecrease(habit) },
                    Color(0xffe18b8d),
                    minus,
                    ""
                )
                HabitActionButton(
                    modifier
                        .clip(RoundedCornerShape(35)),
                    onClickable = { onIncrease(habit) },
                    Color(0xff7bf1f3),
                    PlusSmall,
                    ""
                )
                HabitActionButton(
                    modifier
                        .clip(RoundedCornerShape(35)),
                    onClickable = { onDone(habit) },
                    Color(0xff7bf18d),
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
            .background(SolidColor(backGroundColor)),
        enabled = isActive
    ) {
        Icon(
            icon,
            contentDescription,
            modifier = Modifier
                .clip(RoundedCornerShape(25)),
            tint = Color.Black
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