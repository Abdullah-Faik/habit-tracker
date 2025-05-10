package com.fola.habit_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fola.habit_tracker.ui.auth.HomeScreen
import com.fola.habit_tracker.ui.auth.WelcomeScreen
import com.fola.habit_tracker.ui.main.habit_screen.AddingHabitScreen
import com.fola.habit_tracker.ui.main.home.HomeViewModel
import com.fola.habit_tracker.ui.main.navigation_bar.MainApp
import com.fola.habit_tracker.ui.task_screen.TaskNavigation
import com.fola.habit_tracker.ui.theme.AppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
               // AddingHabitScreen()
                MainApp()
            }
        }
    }
}
