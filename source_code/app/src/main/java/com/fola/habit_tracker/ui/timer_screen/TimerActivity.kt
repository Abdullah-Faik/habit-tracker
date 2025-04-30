package com.fola.habit_tracker.ui.timer_screen


import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fola.habit_tracker.ui.theme.AppTheme
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.text.format

class TimerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                TimerScreen()

            }
        }
    }
}
@Composable
fun MyApp() {
    MaterialTheme {
        TimerScreen(
            totalTime = 7200L, // 1 minute timer
            initialTime = 0L // Start from 0
        )
    }
}


@Composable
fun TimerScreen(
    totalTime: Long = 3600L, // Total time in seconds
    initialTime: Long = 0L, // Initial time in seconds
    circleSize: Dp = 300.dp,
    circleThickness: Dp = 10.dp,
    onTimerFinished: () -> Unit = {}
) {
    // Animation progress (0f to 1f)
    val progress = remember { Animatable(initialTime.toFloat() / totalTime.toFloat()) }
    val scope = rememberCoroutineScope()

    // Current time in seconds (derived from progress)
    val currentTime = remember { (totalTime * (1 - progress.value)).toLong() }

    // Colors for dark theme
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.onPrimary
    val backgroundColor = MaterialTheme.colorScheme.surface // Dark background
    val textColor = MaterialTheme.colorScheme.onSurface      // White text

    // Start or pause the timer
    fun toggleTimer() {
        scope.launch {
            if (progress.isRunning) {
                progress.stop()
            } else {
                if (progress.value >= 1f) {
                    // Reset if finished
                    progress.snapTo(0f)
                }
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = ((totalTime - currentTime) * 1000).toInt(),
                        easing = LinearEasing
                    )
                )
                onTimerFinished()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
       // contentAlignment = Alignment.Center
    ) {
        Spacer(modifier = Modifier.height(70.dp))
        Box(
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            // Background circle
            Canvas(
                modifier = Modifier.size(circleSize)
            ) {
                drawArc(
                    color = secondaryColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = circleThickness.toPx(), cap = StrokeCap.Round),
                    size = Size(size.width, size.height)
                )
            }

            // Progress circle
            Canvas(
                modifier = Modifier.size(circleSize)

            ) {
                drawArc(
                    color = primaryColor,
                    startAngle = -40f,
                    sweepAngle = 360f * progress.value,
                    useCenter = false,
                    style = Stroke(width = circleThickness.toPx(), cap = StrokeCap.Round),
                    size = Size(size.width, size.height)
                )
            }
            Column(modifier = Modifier.align(Alignment.Center)) {
                // Timer text
                Text(
                    modifier = Modifier,//.align(Alignment.Center),
                    text = formatTime(currentTime),
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Text(
                    modifier = Modifier,//.align(Alignment.Center),
                    text = "Total : ${formatTime(totalTime)} Minutes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))

        val context = LocalContext.current
        Box(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            
        ) {
            // Control button (positioned below the circle)
            Row(
                modifier = Modifier,
            ) {
                //start button
                ClickableIcon(
                    iconResId = R.drawable.stopbtn,
                    onClick = { /* Handle click */

                        Toast.makeText(context, "stoped", Toast.LENGTH_SHORT).show()
                    },
                    tint = MaterialTheme.colorScheme.primary,
                    size = 48.dp,
                )
                Spacer(modifier = Modifier.width(16.dp))
                //reset button
                ClickableIcon(
                    iconResId = R.drawable.resetbtn,
                    onClick = { /* Handle click */

                        Toast.makeText(context, "reset", Toast.LENGTH_SHORT).show()
                    },
                    tint = MaterialTheme.colorScheme.primary,
                    size = 48.dp
                )
                Spacer(modifier = Modifier.width(16.dp))
                //pause button
                ClickableIcon(
                    iconResId = R.drawable.pausebtn,
                    onClick = { /* Handle click */

                        Toast.makeText(context, "paused", Toast.LENGTH_SHORT).show()
                    },
                    tint = MaterialTheme.colorScheme.primary,
                    size = 48.dp
                )

            }
        }
    }
}

@Composable
fun ClickableIcon(
    iconResId: Int,
    onClick: () -> Unit,
    tint: Color = Color.White,
    size: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = iconResId),
        contentDescription = "Clickable icon",
        tint = tint,
        modifier = modifier
            .size(size)
            .clickable(
                onClick = onClick,
                // Adds ripple effect
               // indication = rememberRipple()
            )
    )
}
@SuppressLint("DefaultLocale")
private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TimerPreview() {
    AppTheme {
        TimerScreen()

    }
}
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TimerDarkPreview() {
    AppTheme {
        TimerScreen()

    }
}