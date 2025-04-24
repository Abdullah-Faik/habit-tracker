package com.fola.habit_tracker.ui.task_screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun TaskNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "TaskScreen"
    ) {
        composable(route = "TaskScreen") {
            SingleTaskScreen(navController)
        }
        composable(route = "addingTask") {
            AddingTaskScreen(navController)
        }
    }
}