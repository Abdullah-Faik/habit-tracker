package com.fola.habit_tracker.ui.main.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.fola.habit_tracker.R
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.ui.components.icons.PlusSmall
import com.fola.habit_tracker.ui.components.mainFont
import com.fola.habit_tracker.ui.theme.AppTheme
import kotlinx.coroutines.flow.flow
import java.time.LocalDate


@OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {

    

    val habits = viewModel.habits.collectAsState()
    val dailyHabit = viewModel.dailyHabits.collectAsState()
    val day = viewModel.day.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            Icon(
                imageVector = PlusSmall,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .size(56.dp)
                    .clickable {
                        viewModel.addNewHabit(Habit(title = "test"))
                    },
            )

        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ProfileCard()
            DateRow(
                modifier = Modifier
                    .padding(vertical = 4.dp),
                onCardPress = {
                    viewModel.setCurrentDate(it)
                },
                selectedDay = day.value.dayId
            )
            ProgressCard(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                progress = viewModel.getDayProgress(day.value.dayId),
                allTasks = habits.value.habits.size,
                completedTask = viewModel.getCompletedHabit()
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(items = habits.value.habits, key = { it.id }) { habit ->
                    Box(modifier = Modifier.padding(vertical = 4.dp)) {

                        TasksCard(
                            modifier = Modifier,
                            habit = habit,
                            progress = viewModel.getDailyHabitProgress(
                                dayId = day.value.dayId,
                                habitId = habit.id
                            ),
                            onClickable = {
                                Log.d("clicking", "clicked")
                                viewModel.removeDailyHabit(LocalDate.now(), habit.id)
                            },
                            onIncrease = {},
                            onDecrease = {},
                            onDone = {},
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            viewModel = HomeViewModel(FakeHabitsRepository())
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenDarkPreview() {
    AppTheme {
        HomeScreen(
            viewModel = HomeViewModel(FakeHabitsRepository())
        )
    }
}