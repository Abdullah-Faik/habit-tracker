package com.fola.habit_tracker.ui.main.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.ui.components.icons.PlusSmall
import com.fola.habit_tracker.ui.main.navigation_bar.HabitSubRoutes
import com.fola.habit_tracker.ui.main.navigation_bar.Screen
import com.fola.habit_tracker.ui.main.profileScreen.LocalProfileRepository.userProfile
import com.fola.habit_tracker.ui.theme.AppTheme
import java.time.LocalDate


@OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {

    val userProfile by userProfile.collectAsStateWithLifecycle()
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
                        Log.d("HomeScreen", "Navigating to habit/adding_habit")
                        navController.navigate(
                            Screen.Habit.createHabitRoute(HabitSubRoutes.ADDING_HABIT)
                        )
                    }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ProfileCard(
                userName = userProfile.name.takeIf { it.isNotEmpty() } ?: "Guest User",
                profileImageUri = userProfile.profileImageUri
            )
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
                            onIncrease = {viewModel.increase(it)},
                            onDecrease = {viewModel.deCrease(it)},
                            onDone = {viewModel.markDone(it)},
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
            //viewModel = HomeViewModel(FakeHabitsRepository())
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
          //  viewModel = HomeViewModel(FakeHabitsRepository())
        )
    }
}