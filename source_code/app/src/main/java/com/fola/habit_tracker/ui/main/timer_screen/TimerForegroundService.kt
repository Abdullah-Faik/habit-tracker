package com.fola.habit_tracker.ui.main.timer_screen

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.fola.habit_tracker.MainActivity
import com.fola.habit_tracker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TimerForegroundService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var timerJob: Job? = null
    private lateinit var viewModel: TimerViewModel
    private var isServiceRunning = false
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val ACTION_START = "com.fola.habit_tracker.ACTION_START"
        const val ACTION_STOP = "com.fola.habit_tracker.ACTION_STOP"
        const val EXTRA_DURATION = "com.fola.habit_tracker.EXTRA_DURATION"
        const val NOTIFICATION_ID = 2
        const val CHANNEL_ID = "TIMER_FOREGROUND_CHANNEL_ID"
    }

    override fun onCreate() {
        super.onCreate()
        try {
            viewModel = TimerViewModel()
            createNotificationChannel()
            // Initialize MediaPlayer for timer end sound
            mediaPlayer = MediaPlayer.create(this, R.raw.timer_end)?.apply {
                setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                    Log.d("TimerForegroundService", "MediaPlayer released after completion")
                }
            } ?: run {
                Log.e("TimerForegroundService", "Failed to initialize MediaPlayer for timer_end")
                null
            }
        } catch (e: Exception) {
            Log.e("TimerForegroundService", "Failed to initialize service: ${e.message}")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            when (intent?.action) {
                ACTION_START -> {
                    if (!isServiceRunning) {
                        val duration = intent.getLongExtra(EXTRA_DURATION, 60000L)
                        Log.d("TimerForegroundService", "Received duration: $duration ms")
                        viewModel.setTotalDuration(duration)
                        startForegroundService()
                        isServiceRunning = true
                    }
                }
                ACTION_STOP -> {
                    stopForegroundService()
                }
            }
        } catch (e: Exception) {
            Log.e("TimerForegroundService", "Error in onStartCommand: ${e.message}")
        }
        return START_STICKY
    }

    private fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            Log.d("TimerForegroundService", "POST_NOTIFICATIONS permission: $granted")
            granted
        } else {
            true
        }
    }

    private fun startForegroundService() {
        try {
            if (!viewModel.timerState.value.isRunning && viewModel.timerState.value.remainingTime > 0) {
                Log.d("TimerForegroundService", "Starting timer with remaining time: ${viewModel.timerState.value.remainingTime}")
                viewModel.startTimer()
            } else {
                Log.w("TimerForegroundService", "Timer not started: isRunning=${viewModel.timerState.value.isRunning}, remainingTime=${viewModel.timerState.value.remainingTime}")
            }

            if (checkNotificationPermission()) {
                startForeground(NOTIFICATION_ID, buildNotification(viewModel.timerState.value.remainingTime))
            } else {
                Log.w("TimerForegroundService", "Cannot start foreground service: POST_NOTIFICATIONS permission denied")
                stopSelf()
                return
            }

            timerJob = coroutineScope.launch {
                viewModel.timerState.collect { state ->
                    if (state.isRunning && state.remainingTime > 0) {
                        updateNotification(state.remainingTime)
                        delay(1000)
                    } else if (state.remainingTime <= 0 && state.totalDuration > 0) {
                        if (checkNotificationPermission()) {
                            try {
                                sendTimerCompleteNotification()
                                Log.d("TimerForegroundService", "Sent completion notification")
                            } catch (e: Exception) {
                                Log.e("TimerForegroundService", "Failed to send completion notification: ${e.message}")
                            }
                        } else {
                            Log.w("TimerForegroundService", "Cannot send completion notification: POST_NOTIFICATIONS permission denied")
                        }
                        try {
                            mediaPlayer?.start() ?: run {
                                // Reinitialize MediaPlayer if it was released
                                mediaPlayer =
                                    MediaPlayer.create(this@TimerForegroundService, R.raw.timer_end)
                                        ?.apply {
                                            setOnCompletionListener {
                                                it.release()
                                                mediaPlayer = null
                                                Log.d(
                                                    "TimerForegroundService",
                                                    "MediaPlayer reinitialized and released after completion"
                                                )
                                            }
                                            start()
                                        }
                                Log.w(
                                    "TimerForegroundService",
                                    "MediaPlayer was null, reinitialized for timer end sound"
                                )
                            }
                            Log.d("TimerForegroundService", "Playing timer end sound")
                        } catch (e: Exception) {
                            Log.e(
                                "TimerForegroundService",
                                "Failed to play timer end sound: ${e.message}"
                            )
                        }
                        stopForegroundService()
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("TimerForegroundService", "SecurityException in startForegroundService: ${e.message}")
            stopSelf()
        } catch (e: Exception) {
            Log.e("TimerForegroundService", "Error in startForegroundService: ${e.message}")
            stopSelf()
        }
    }

    private fun stopForegroundService() {
        try {
            timerJob?.cancel()
            viewModel.stopTimer()
            isServiceRunning = false
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        } catch (e: Exception) {
            Log.e("TimerForegroundService", "Error in stopForegroundService: ${e.message}")
        }
    }

    private fun createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Channel for ongoing timer notification
                val name = "Timer Foreground Service"
                val descriptionText = "Shows timer progress in the foreground"
                val importance = NotificationManager.IMPORTANCE_LOW
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                // Channel for timer completion with sound and vibration
                val completionChannel = NotificationChannel(
                    "TIMER_CHANNEL_ID",
                    "Timer Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for timer completion"
                    enableLights(true)
                    lightColor = android.graphics.Color.GREEN
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(completionChannel)
            }
        } catch (e: Exception) {
            Log.e("TimerForegroundService", "Failed to create notification channel: ${e.message}")
        }
    }

    private fun buildNotification(remainingTime: Long): Notification {
        try {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("navigate_to", "timer")
            }
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )

            val stopIntent = Intent(this, TimerForegroundService::class.java).apply {
                action = ACTION_STOP
            }
            val stopPendingIntent = PendingIntent.getService(
                this,
                1,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )

            return NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.yoga_girl)
                .setContentTitle("Timer Running")
                .setContentText("Time remaining: ${formatMillisToTime(remainingTime)}")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopPendingIntent)
                .setOngoing(true)
                .build()
        } catch (e: Exception) {
            Log.e("TimerForegroundService", "Failed to build notification: ${e.message}")
            return NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Timer Running")
                .setContentText("Time remaining: ${formatMillisToTime(remainingTime)}")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build()
        }
    }

    private fun updateNotification(remainingTime: Long) {
        try {
            if (checkNotificationPermission()) {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NOTIFICATION_ID, buildNotification(remainingTime))
            } else {
                Log.w("TimerForegroundService", "Cannot update notification: POST_NOTIFICATIONS permission denied")
            }
        } catch (e: SecurityException) {
            Log.e("TimerForegroundService", "SecurityException in updateNotification: ${e.message}")
        } catch (e: Exception) {
            Log.e("TimerForegroundService", "Failed to update notification: ${e.message}")
        }
    }

    private fun sendTimerCompleteNotification() {
        try {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("navigate_to", "timer")
            }
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
            )

            val builder = NotificationCompat.Builder(this, "TIMER_CHANNEL_ID")
                .setSmallIcon(R.drawable.yoga_girl)
                .setContentTitle("Timer Complete")
                .setContentText("Your timer has finished!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE) // Add default sound and vibration

            with(NotificationManagerCompat.from(this)) {
                notify(1, builder.build())
            }
        } catch (e: SecurityException) {
            Log.e("TimerForegroundService", "SecurityException in sendTimerCompleteNotification: ${e.message}")
        } catch (e: Exception) {
            Log.e("TimerForegroundService", "Failed to send completion notification: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            coroutineScope.cancel()
            isServiceRunning = false
            mediaPlayer?.release()
            mediaPlayer = null
            Log.d("TimerForegroundService", "Service destroyed, MediaPlayer released")
        } catch (e: Exception) {
            Log.e("TimerForegroundService", "Error in onDestroy: ${e.message}")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun formatMillisToTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}