package com.fola.habit_tracker.ui.main.navigation_bar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.ui.main.TasksScreen.HabitScree
import com.fola.habit_tracker.ui.main.calender.CalenderScreen
import com.fola.habit_tracker.ui.main.home.HomeScreen
import com.fola.habit_tracker.ui.main.profile.ProfileScreen
import com.fola.habit_tracker.ui.main.timer.TimerScreen


@Composable
fun MainApp(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        bottomBar = { BottomNavigationBar(
            navController = navController
        ) }
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Habit.route) { HabitScree() }
            composable(Screen.Timer.route) { TimerScreen() }
            composable(Screen.Calendar.route) { CalenderScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }

    }


}


@Preview
@Composable
private fun MainAppPrev() {
    MainApp()
}


