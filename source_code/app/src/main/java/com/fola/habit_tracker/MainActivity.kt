package com.fola.habit_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fola.habit_tracker.ui.main.navigation_bar.EntryPoint
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        setContent {
            EntryPoint(navigateToRoute = intent?.getStringExtra("navigate_to"))
        }
    }
}