package com.fola.habit_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fola.habit_tracker.ui.auth.WelcomeScreen
import com.fola.habit_tracker.ui.main.navigation_bar.MainApp
import com.fola.habit_tracker.ui.theme.AppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainApp()
            }
        }
    }
}
