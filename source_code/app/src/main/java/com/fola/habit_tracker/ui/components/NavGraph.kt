package com.fola.habit_tracker.ui.task_screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.ui.main.habit_screen.AddingTaskScreen
import com.fola.habit_tracker.ui.main.habit_screen.HabitScreen

// The navigation Between Task Screen & Adding Task Screen

@Composable
fun TaskNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "TaskScreen"
    ) {
        composable(route = "TaskScreen") {
            HabitScreen(navController)
        }
        composable(route = "addingTask") {
            AddingTaskScreen(navController)
        }
    }
}