package com.fola.habit_tracker.ui.main.habit_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.data.database.RepeatedType
import com.fola.habit_tracker.ui.components.icons.settings
import com.fola.habit_tracker.ui.components.icons.trash
import com.fola.habit_tracker.ui.components.interFont
import com.fola.habit_tracker.ui.main.home.HabitActionButton
import com.fola.habit_tracker.ui.theme.AppTheme
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HabitControlCard(
    modifier: Modifier = Modifier,
    habit: Habit,
    onClickable: () -> Unit = {},
    onDelete: (Habit) -> Unit = {},
    onEdit: (Habit) -> Unit = {},
    onNotif: (Habit) -> Unit = {},
) {


    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SolidColor(Color(habit.color)), alpha = 0.2f)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(.7f)
                    .padding(vertical = 8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(habit.icon),
                        contentDescription = habit.iconDescription,
                        modifier = Modifier
                            .size(64.dp)
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
                        Text(
                            text = habit.description,
                            fontFamily = interFont,
                        )
                    }
                }
                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(10))
                        .background(Color.White)
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                ) {
                    Spacer(Modifier.padding(horizontal = 2.dp))
                    Text(
                        "Repeated Type: ${habit.repeatedType.name.lowercase().capitalize()}"
                    )
                    Text(
                        "Notification Time: ${habit.startTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}"
                    )
                    Text(
                        "Goal: ${habit.timesOfUnit} ${habit.unit}"
                    )
                    Text(
                        if (habit.repeatedType == RepeatedType.WEEKLY) {
                            habit.days.joinToString(", ", prefix = "Days : ", postfix = "") { day ->
                                DayOfWeek(day).name.take(3)
                            }
                        } else ""
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(.1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)

                ) {
                IconButton(
                    onClick = { onNotif(habit) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(25))
                        .background(SolidColor(Color(0xff7bf18d)))
                )
                {
                    Icon(
                        imageVector = if (habit.notification == 1) Icons.Default.Notifications else
                            Icons.Default.NotificationsOff,
                        "",
                        tint = Color.Black
                    )

                }

                HabitActionButton(
                    modifier
                        .clip(RoundedCornerShape(35)),
                    onClickable = { onDelete(habit) },
                    Color(0xffe18b8d),
                    trash,
                    ""
                )
                HabitActionButton(
                    modifier
                        .clip(RoundedCornerShape(35)),
                    onClickable = { onEdit(habit) },
                    Color(0xff7bf1f3),
                    settings,
                    ""
                )
            }
        }
    }
}


val TestHabit = Habit(
    title = "test",
    description = "gfgdfgsdfgsdfgsfd",
    startDate = LocalDate.now(),
    endDate = LocalDate.now(),
    icon = R.drawable.eating,
    iconDescription = "",
    notification = 0,
    repeatedType = RepeatedType.WEEKLY,
    days = mutableSetOf<Int>(1, 3, 5),
    unit = "glass",
    timesOfUnit = 8
)

@Preview
@Composable
private fun HabitControlCardPrev() {
    AppTheme {
        HabitControlCard(habit = TestHabit)
    }
}