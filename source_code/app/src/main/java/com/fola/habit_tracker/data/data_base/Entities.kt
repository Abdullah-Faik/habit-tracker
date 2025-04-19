package com.fola.habit_tracker.data.data_base

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalTime
import java.util.Date


@Entity(tableName = "task")
data class Task(
    @PrimaryKey val id: Long = Date().time,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "start_date") val startDate: Date,
    @ColumnInfo(name = "done_date") val finishDate: Date?,
    @ColumnInfo(name = "is_done") val isDone: Boolean = false,
    @ColumnInfo(name = "is_removed") val isRemoved: Boolean = false
)


enum class RepeatedType {
    NONE, DAILY, WEEKLY, MONTHLY, YEARLY
}

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey val id: Long = Date().time,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "icon_path") @DrawableRes val iconPath: Int = 0,
    @ColumnInfo(name = "reminder_time") val reminderTime: LocalTime = LocalTime.of(12, 0),
    @ColumnInfo(name = "repeated_type") val repeatedType: RepeatedType = RepeatedType.DAILY,
    @ColumnInfo(name = "repeats_per_day") val repeatPerDay: Int = 1,
    @ColumnInfo(name = "is_removed") val isRemoved: Boolean = false,
    @ColumnInfo(name = "remove_date") val removedDate: Date? = null
)

@Entity(tableName = "day")
data class Day(
    @PrimaryKey val dayId: String,
    val progress: Float = 0f,
)


@Entity(tableName = "daily_habit", primaryKeys = ["dayId", "id"])
data class DailyHabits(
    val dayId: String = "",
    val id: Int = 0,
    val isDone: Boolean = false
)

data class DayWithHabits(
    @Embedded val day: Day,
    @Relation(
        parentColumn = "dayId",
        entityColumn = "id",
        associateBy = Junction(DailyHabits::class)
    ) val habits: List<Habit>
)