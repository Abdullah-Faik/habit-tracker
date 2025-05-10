package com.fola.habit_tracker.ui.main.timer_screen

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.R
import java.util.concurrent.TimeUnit

@Composable
fun TimerScreen(viewModel: TimerViewModel, navController: NavController) {
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // State to track if the sound has been played
    var hasPlayedSound by remember { mutableStateOf(false) }

    // Initialize MediaPlayer for the timer end sound
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.timer_end).apply {
            setOnCompletionListener {
                it.release() // Release MediaPlayer after playback
            }
        }
    }

    // Clean up MediaPlayer when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    // Start the timer automatically when the screen is first composed
    LaunchedEffect(Unit) {
        if (!timerState.isRunning && timerState.remainingTime > 0) {
            viewModel.startTimer()
        }
    }

    // Observe remaining time to detect when the timer ends
    LaunchedEffect(timerState.remainingTime) {
        if (timerState.remainingTime <= 0 && timerState.totalDuration > 0) {
            // Timer has ended
            if (!hasPlayedSound) {
                mediaPlayer?.start() // Play the sound
                hasPlayedSound = true // Prevent playing multiple times
            }
            // Navigate back to SetTimerScreen
            navController.navigate("timer") {
                popUpTo("timer") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Surface(
        color = Color(0xFF121212),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TimerProgress(
                    progress = timerState.progress,
                    remainingTime = timerState.remainingTime,
                    totalDuration = timerState.totalDuration,
                    modifier = Modifier.size(300.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 55.dp, vertical = 50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        viewModel.stopTimer()
                        viewModel.resetTimer()
                        hasPlayedSound = false // Reset sound state
                        navController.navigate("timer") {
                            popUpTo("timer") { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop", tint = Color(0xFF4DB6AC))
                    }
                    IconButton(onClick = {
                        viewModel.restartTimer()
                        hasPlayedSound = false // Reset sound state
                    }) {
                        Icon(imageVector = Icons.Default.Replay, contentDescription = "Restart", tint = Color(0xFF4DB6AC))
                    }
                    IconButton(onClick = {
                        if (timerState.isRunning) viewModel.pauseTimer() else viewModel.resumeTimer()
                        hasPlayedSound = false // Reset sound state
                    }) {
                        Icon(
                            imageVector = if (timerState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (timerState.isRunning) "Pause" else "Play",
                            tint = Color(0xFF4DB6AC)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimerProgress(
    progress: Float,
    remainingTime: Long,
    totalDuration: Long,
    modifier: Modifier = Modifier,
    indicatorColor: Color = Color(0xFF4DB6AC),
    trackColor: Color = Color(0xFF2D3748),
    strokeWidth: Dp = 12.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = androidx.compose.animation.core.tween(300)
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = indicatorColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatMillisToTime(remainingTime),
                color = Color.White,
                fontSize = 48.sp
            )
            Text(
                text = "Total ${TimeUnit.MILLISECONDS.toMinutes(totalDuration)} minutes",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatMillisToTime(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

@Preview
@Composable
fun TimerScreenPreview(modifier: Modifier = Modifier) {
    TimerScreen(viewModel = viewModel(), navController = rememberNavController())
}