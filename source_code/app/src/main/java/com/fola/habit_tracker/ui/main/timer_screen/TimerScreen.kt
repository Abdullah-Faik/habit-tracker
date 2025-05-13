package com.fola.habit_tracker.ui.main.timer_screen

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.R
import com.fola.habit_tracker.ui.theme.AppTheme
import java.util.concurrent.TimeUnit

@Composable
fun TimerScreen(viewModel: TimerViewModel, navController: NavController) {
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()

    // State to track notification and dialog
    var showDialog by remember { mutableStateOf(false) }
    var hasNotificationPermission by remember { mutableStateOf(checkNotificationPermission(context)) }

    // MediaPlayer for timer completion sound
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Request POST_NOTIFICATIONS permission for Android 13+
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        Log.d("TimerScreen", "POST_NOTIFICATIONS permission granted: $isGranted")
        hasNotificationPermission = isGranted
    }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Create Notification Channel
    LaunchedEffect(Unit) {
        createNotificationChannel(context)
    }

    // Start the timer and foreground service automatically
    LaunchedEffect(timerState.totalDuration) {
        if (!timerState.isRunning && timerState.remainingTime > 0) {
            Log.d("TimerScreen", "Starting timer with duration: ${timerState.totalDuration} ms")
            viewModel.startTimer()
            try {
                context.startService(Intent(context, TimerForegroundService::class.java).apply {
                    action = TimerForegroundService.ACTION_START
                    putExtra(TimerForegroundService.EXTRA_DURATION, timerState.totalDuration)
                })
            } catch (e: Exception) {
                Log.e("TimerScreen", "Failed to start TimerForegroundService: ${e.message}")
            }
        } else {
            Log.w("TimerScreen", "Service not started: isRunning=${timerState.isRunning}, remainingTime=${timerState.remainingTime}")
        }
    }

    // Observe remaining time to detect when the timer ends
    LaunchedEffect(timerState.remainingTime) {
        if (timerState.remainingTime <= 0 && timerState.totalDuration > 0) {
            showDialog = true
            // Play sound when timer completes in foreground
            if (mediaPlayer == null) {
                try {
                    mediaPlayer = MediaPlayer.create(context, R.raw.timer_end)?.apply {
                        setOnCompletionListener {
                            it.release()
                            mediaPlayer = null
                            Log.d("TimerScreen", "MediaPlayer released after completion")
                        }
                        start()
                        Log.d("TimerScreen", "Playing timer end sound")
                    }
                } catch (e: Exception) {
                    Log.e("TimerScreen", "Failed to play timer end sound: ${e.message}")
                }
            }
        }
    }

    // Clean up MediaPlayer when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
            Log.d("TimerScreen", "MediaPlayer released on dispose")
        }
    }

    // Dialog for timer completion
    if (showDialog) {
        TimerCompleteDialog(
            onDismiss = {
                showDialog = false
                viewModel.resetTimer()
                try {
                    context.startService(Intent(context, TimerForegroundService::class.java).apply {
                        action = TimerForegroundService.ACTION_STOP
                    })
                } catch (e: Exception) {
                    Log.e("TimerScreen", "Failed to stop TimerForegroundService: ${e.message}")
                }
                navController.navigate("timer") {
                    launchSingleTop = true
                }
            },
            viewModel = viewModel,
            navController = navController,
            isDarkTheme = isDarkTheme
        )
    }

    AppTheme {
        Surface(
            color = if (isDarkTheme) Color(0xFF121212) else MaterialTheme.colorScheme.background,
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
                        modifier = Modifier.size(300.dp),
                        isDarkTheme = isDarkTheme
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 55.dp, vertical = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.stopTimer()
                                viewModel.resetTimer()
                                showDialog = false
                                try {
                                    context.startService(Intent(context, TimerForegroundService::class.java).apply {
                                        action = TimerForegroundService.ACTION_STOP
                                    })
                                } catch (e: Exception) {
                                    Log.e("TimerScreen", "Failed to stop TimerForegroundService: ${e.message}")
                                }
                                navController.navigate("timer") {
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    if (isDarkTheme) Color(0xFF177882) else MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(16.dp)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {
                                viewModel.restartTimer()
                                showDialog = false
                                try {
                                    context.startService(Intent(context, TimerForegroundService::class.java).apply {
                                        action = TimerForegroundService.ACTION_START
                                        putExtra(TimerForegroundService.EXTRA_DURATION, timerState.totalDuration)
                                    })
                                } catch (e: Exception) {
                                    Log.e("TimerScreen", "Failed to restart TimerForegroundService: ${e.message}")
                                }
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    if (isDarkTheme) Color(0xFF177882) else MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(16.dp)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription = "Restart",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {
                                if (timerState.isRunning) {
                                    viewModel.pauseTimer()
                                    try {
                                        context.startService(
                                            Intent(
                                                context,
                                                TimerForegroundService::class.java
                                            ).apply {
                                                action = TimerForegroundService.ACTION_STOP
                                            })
                                    } catch (e: Exception) {
                                        Log.e(
                                            "TimerScreen",
                                            "Failed to stop TimerForegroundService on pause: ${e.message}"
                                        )
                                    }
                                } else {
                                    viewModel.resumeTimer()
                                    try {
                                        context.startService(
                                            Intent(
                                                context,
                                                TimerForegroundService::class.java
                                            ).apply {
                                                action = TimerForegroundService.ACTION_START
                                                putExtra(
                                                    TimerForegroundService.EXTRA_DURATION,
                                                    timerState.remainingTime
                                                )
                                            })
                                    } catch (e: Exception) {
                                        Log.e(
                                            "TimerScreen",
                                            "Failed to start TimerForegroundService on resume: ${e.message}"
                                        )
                                    }
                                }
                                showDialog = false
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    if (isDarkTheme) Color(0xFF177882) else MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(16.dp)
                                )
                        ) {
                            Icon(
                                imageVector = if (timerState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (timerState.isRunning) "Pause" else "Play",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimerCompleteDialog(
    onDismiss: () -> Unit,
    viewModel: TimerViewModel,
    navController: NavController,
    isDarkTheme: Boolean
) {
    AlertDialog(
        onDismissRequest = { /* Prevent dismissing by clicking outside */ },
        modifier = Modifier
            .background(
                Brush.linearGradient(
                    colors = if (isDarkTheme) listOf(Color(0xFF4DB6AC), Color(0xFF177882))
                    else listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, 400f)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        confirmButton = {},
        dismissButton = {},
        title = {
            Text(
                text = "Timer Complete!",
                color = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.yoga_girl),
                    contentDescription = "Yoga Girl",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "Great job! Your timer has finished.",
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = {
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkTheme) Color(0xFF121212) else MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = if (isDarkTheme) Color(0xFF4DB6AC) else MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Stop",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        containerColor = Color.Transparent
    )
}

@Composable
fun TimerProgress(
    progress: Float,
    remainingTime: Long,
    totalDuration: Long,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    strokeWidth: Dp = 16.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = androidx.compose.animation.core.tween(500)
    )

    // Define colors based on theme
    val indicatorColor = if (isDarkTheme) Color(0xFF4DB6AC) else MaterialTheme.colorScheme.primary
    val trackColor = if (isDarkTheme) Color(0xFF2D3748) else MaterialTheme.colorScheme.surfaceContainerHighest
    val gradientEndColor = if (isDarkTheme) Color(0xFF80CBC4) else MaterialTheme.colorScheme.primaryContainer
    val outerArcColor = if (isDarkTheme) Color.Black.copy(alpha = 0.1f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    val textColor = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = if (isDarkTheme) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

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
            val gradientBrush = Brush.linearGradient(
                colors = listOf(
                    indicatorColor,
                    gradientEndColor
                ),
                start = center,
                end = center.copy(y = size.height)
            )
            drawArc(
                brush = gradientBrush,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = outerArcColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = (strokeWidth + 2.dp).toPx(), cap = StrokeCap.Round),
                size = Size(size.width - 4.dp.toPx(), size.height - 4.dp.toPx())
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatMillisToTime(remainingTime),
                color = textColor,
                fontSize = 48.sp
            )
            Text(
                text = "Total ${TimeUnit.MILLISECONDS.toMinutes(totalDuration)} minutes",
                color = secondaryTextColor,
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

private fun createNotificationChannel(context: Context) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer Notifications"
            val descriptionText = "Notifications for timer completion"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("TIMER_CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    } catch (e: Exception) {
        Log.e("TimerScreen", "Failed to create notification channel: ${e.message}")
    }
}

private fun checkNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        Log.d("TimerScreen", "POST_NOTIFICATIONS permission: $granted")
        granted
    } else {
        true
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenLightPreview() {
    AppTheme(darkTheme = false) {
        TimerScreen(viewModel = viewModel(), navController = rememberNavController())
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TimerScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        TimerScreen(viewModel = viewModel(), navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun TimerCompleteDialogLightPreview() {
    AppTheme(darkTheme = false) {
        TimerCompleteDialog(
            onDismiss = {},
            viewModel = viewModel(),
            navController = rememberNavController(),
            isDarkTheme = false
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TimerCompleteDialogDarkPreview() {
    AppTheme(darkTheme = true) {
        TimerCompleteDialog(
            onDismiss = {},
            viewModel = viewModel(),
            navController = rememberNavController(),
            isDarkTheme = true
        )
    }
}