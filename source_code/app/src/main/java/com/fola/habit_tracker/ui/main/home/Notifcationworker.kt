package com.fola.habit_tracker.ui.main.home


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.fola.habit_tracker.HabitDataBaseProvider
import com.fola.habit_tracker.R
import com.fola.habit_tracker.data.database.Habit
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class NotificationWorker(
    private val appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val habitId = inputData.getLong("habit_id", -1)
        val title = inputData.getString("title") ?: "Habit Reminder"
        val description = inputData.getString("description") ?: "Time to work on your habit!"

        // Send the notification
        sendNotification(title, description)

        // Reschedule for the next occurrence (e.g., tomorrow for DAILY)
        val repository = HabitDataBaseProvider.getDatabase(appContext)
        val habit = repository.habitDao().getHabit(habitId)
        if (habit != null && habit.notification == 1 && habit.isInactive == 0 && habit.isRemoved == 0) {
            scheduleNextNotification(habit)
        }

        return Result.success()
    }

    private fun sendNotification(title: String, description: String) {
        val channelId = "habit_notifications"
        val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel
        val channel = NotificationChannel(
            channelId,
            "Habit Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        // Build notification
        val notification = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(R.drawable.yoga_girl) // Replace with your icon
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun scheduleNextNotification(habit: Habit) {
        val now = LocalDateTime.now()
        var nextTime = LocalDateTime.of(now.toLocalDate(), habit.startTime).plusDays(1)
        val delayMinutes = ChronoUnit.MINUTES.between(now, nextTime)

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .setInputData(
                workDataOf(
                    "habit_id" to habit.id,
                    "title" to habit.title,
                    "description" to habit.description
                )
            )
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            "habit_notification_${habit.id}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}