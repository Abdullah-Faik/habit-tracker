package com.fola.habit_tracker.data.data_base

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date


enum class RepeatedType {
    TASK, DAILY, WEEKLY, MONTHLY, YEARLY
}


@Entity(tableName = "habit")
data class Habit(
    @ColumnInfo(name = "habit_id") @PrimaryKey val id: Long = Date().time,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "icon_path") @DrawableRes val iconPath: Int = 0,
    @ColumnInfo(name = "habit_duration") val habitDuration: Int = 0,
    @ColumnInfo(name = "reminder_time") val reminderTime: LocalTime = LocalTime.of(12, 0),
    @ColumnInfo(name = "repeated_type") val repeatedType: RepeatedType = RepeatedType.DAILY,
    @ColumnInfo(name = "repeats_per_day") val repeatPerDay: Int = 1,
    @ColumnInfo(name = "is_removed") val isRemoved: Int = 0,
    @ColumnInfo(name = "remove_date") val removedDate: Date? = null,
)

@Entity(tableName = "day")
data class Day(
    @ColumnInfo(name = "day_id") @PrimaryKey val dayId: LocalDate = LocalDate.now(),
    val progress: Float = 0f,
)

@Entity(tableName = "daily_habit", primaryKeys = ["day_id", "habit_id"])
data class DailyHabits(
    @ColumnInfo(name = "day_id") val dayId: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "habit_id") val id: Long = 0,
    @ColumnInfo(name = "is_done") val isDone: Boolean = false,
    val progress: Float = 0f,
)

data class DayWithHabits(
    @Embedded val day: Day,
    @Relation(
        parentColumn = "day_id",
        entityColumn = "habit_id",
        associateBy = Junction(DailyHabits::class)
    ) val habits: List<Habit>
)