package com.fola.habit_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.fola.habit_tracker.ui.auth.AuthModule
import com.fola.habit_tracker.ui.auth.HomeScreen
import com.fola.habit_tracker.ui.auth.VerifyScreen
import com.fola.habit_tracker.ui.theme.HabittrackerTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabittrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (auth.currentUser?.isEmailVerified == true)
                        HomeScreen()
                    else if (auth.currentUser != null)
                        VerifyScreen(
                            onStartButton = {
                                auth.signOut()
                            }
                        )
                    else
                        AuthModule(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }


}
