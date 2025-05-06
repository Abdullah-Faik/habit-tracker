package com.fola.habit_tracker.ui.task_screen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.ui.main.habit_screen.AddingHabitScreen
import com.fola.habit_tracker.ui.main.habit_screen.HabitScreen

// The navigation Between Task Screen & Adding Task Screen

@Composable
fun TaskNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "HabitScreen"
    ) {
        composable(route = "HabitScreen") {
            HabitScreen(navController)
        }
        composable(route = "addingHabit") {
            AddingHabitScreen()
        }
    }
}