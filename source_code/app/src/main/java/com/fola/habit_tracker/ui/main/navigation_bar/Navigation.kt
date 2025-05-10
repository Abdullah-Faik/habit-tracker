package com.fola.habit_tracker.ui.main.navigation_bar

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timerscreen.timer_screen.SetTimerScreen
import com.fola.habit_tracker.ui.main.calender.CalenderScreen
import com.fola.habit_tracker.ui.main.habit_screen.HabitScreen
import com.fola.habit_tracker.ui.main.home.HomeScreen
import com.fola.habit_tracker.ui.main.profileScreen.LocalProfileRepository
import com.fola.habit_tracker.ui.main.profileScreen.ProfileScreen
import com.fola.habit_tracker.ui.main.profileScreen.ProfileViewModel
import com.fola.habit_tracker.ui.main.profileScreen.RemoteProfileRepository
import com.fola.habit_tracker.ui.main.timer_screen.TimerScreen
import com.fola.habit_tracker.ui.main.timer_screen.TimerViewModel
import com.fola.habit_tracker.ui.theme.AppTheme

@Composable
fun MainApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val timerViewModel: TimerViewModel = viewModel() // Shared TimerViewModel

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        bottomBar = {
            BottomNavigationBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Habit.route) { HabitScreen(navController) }
            composable(Screen.Timer.route) {
                SetTimerScreen(
                    navController = navController,
                    viewModel = timerViewModel
                )
            }
            composable(
                route = "timer_screen",
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) },
                popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                popExitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                TimerScreen(
                    viewModel = timerViewModel,
                    navController = navController // Pass NavController to TimerScreen
                )
            }
            composable(Screen.Calendar.route) { CalenderScreen() }
            composable(Screen.Profile.route) {
                val viewModel: ProfileViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return ProfileViewModel(LocalProfileRepository(), RemoteProfileRepository()) as T
                        }
                    }
                )
                ProfileScreen()
            }
        }
    }
}

@Preview
@Composable
private fun MainAppPrev() {
    AppTheme {
        MainApp()
    }
}