package com.fola.habit_tracker.ui.main.timer_screen

import android.os.Build
import android.util.Log
import android.widget.NumberPicker
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.R
import com.fola.habit_tracker.ui.theme.AppTheme

@Composable
fun SetTimerScreen(navController: NavController, viewModel: TimerViewModel) {
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }
    val isDarkTheme = isSystemInDarkTheme()

    val totalMillis = (hours * 3600000L) + (minutes * 60000L) + (seconds * 1000L)

    AppTheme {
        Surface(
            color = if (isDarkTheme) Color(0xFF121212) else MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Set Timer",
                    color = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 40.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimePicker(
                        label = "Hours",
                        minValue = 0,
                        maxValue = 23,
                        selectedValue = hours,
                        onValueChange = { hours = it },
                        isDarkTheme = isDarkTheme
                    )
                    TimePicker(
                        label = "Minutes",
                        minValue = 0,
                        maxValue = 59,
                        selectedValue = minutes,
                        onValueChange = { minutes = it },
                        isDarkTheme = isDarkTheme
                    )
                    TimePicker(
                        label = "Seconds",
                        minValue = 0,
                        maxValue = 59,
                        selectedValue = seconds,
                        onValueChange = { seconds = it },
                        isDarkTheme = isDarkTheme
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 48.dp, end = 48.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            hours = 0
                            minutes = 0
                            seconds = 0
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDarkTheme) Color(0xFF177882) else MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Reset",
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(18.dp))

                    Button(
                        onClick = {
                            if (totalMillis > 0) {
                                Log.d("SetTimerScreen", "Setting duration: $totalMillis ms")
                                viewModel.setTotalDuration(totalMillis)
                                navController.navigate("timer_screen")
                            } else {
                                Log.w(
                                    "SetTimerScreen",
                                    "Invalid duration: $totalMillis ms, navigation skipped"
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDarkTheme) Color(0xFF177882) else MaterialTheme.colorScheme.primary,
                            contentColor = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f),
                        enabled = totalMillis > 0
                    ) {
                        Text(
                            text = "Start",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimePicker(
    label: String,
    minValue: Int,
    maxValue: Int,
    selectedValue: Int,
    onValueChange: (Int) -> Unit,
    isDarkTheme: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(150.dp)
                .wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier
                    .height(150.dp)
                    .wrapContentSize(),
                factory = { context ->
                    val themedContext = android.view.ContextThemeWrapper(context, R.style.CustomNumberPicker)
                    NumberPicker(themedContext).apply {
                        this.minValue = minValue
                        this.maxValue = maxValue
                        this.value = selectedValue
                        setFormatter { value -> String.format("%02d", value) }
                        setOnValueChangedListener { _, _, newVal ->
                            onValueChange(newVal)
                        }
                        // Set text color based on theme
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            setTextColor(if (isDarkTheme) Color.White.toArgb() else Color.Black.toArgb())
                        }
                    }
                },
                update = {
                    it.value = selectedValue
                }
            )
        }
        Text(
            text = label,
            color = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SetTimerScreenLightPreview() {
    AppTheme(darkTheme = false) {
        SetTimerScreen(
            navController = rememberNavController(),
            viewModel = viewModel()
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SetTimerScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        SetTimerScreen(
            navController = rememberNavController(),
            viewModel = viewModel()
        )
    }
}