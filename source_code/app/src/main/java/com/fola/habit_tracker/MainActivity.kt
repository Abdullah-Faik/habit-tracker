package com.fola.habit_tracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.fola.habit_tracker.ui.main.home.HomeScreen
import com.fola.habit_tracker.ui.main.navigation_bar.MainApp
import com.fola.habit_tracker.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("here", "here1")

        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold {
                    MainApp(
                        modifier = Modifier.padding(it)
                    )

                }
            }
        }
    }
}
