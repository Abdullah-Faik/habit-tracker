package com.fola.habit_tracker.data.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.fola.habit_tracker.R
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date


enum class RepeatedType {
    ONCE, DAILY, MONTHLY, WEEKLY, YEARLY
}

@Entity(tableName = "habit")
data class Habit(
    @ColumnInfo(name = "habit_id") @PrimaryKey val id: Long = Date().time,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "icon") val icon: Int = R.drawable.programming,
    @ColumnInfo(name = "icon_description") val iconDescription: String = "Coding",
    @ColumnInfo(name = "color") val color: Long = 0xFF03A9F5,
    @ColumnInfo(name = "color_name") val colorName: String = "Light Blue",
    @ColumnInfo(name = "repeated_type") val repeatedType: RepeatedType = RepeatedType.DAILY,
    @ColumnInfo(name = "days") val days: MutableSet<Int> = mutableSetOf(),
    @ColumnInfo(name = "notify") val notification: Int = 1,
    @ColumnInfo(name = "start_date") val startDate: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "end_date") val endDate: LocalDate = LocalDate.of(2099, 12, 31),
    @ColumnInfo(name = "start_time") val startTime: LocalTime = LocalTime.now(),
    @ColumnInfo(name = "times_of_unit") val timesOfUnit: Int = 0,
    @ColumnInfo(name = "unit") val unit: String = "",
    @ColumnInfo(name = "is_inactive") val isInactive: Int = 0,
    @ColumnInfo(name = "is_removed") val isRemoved: Int = 0,
    @ColumnInfo(name = "remove_date") val removedDate: LocalDate = LocalDate.MAX,
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


fun Habit.isOnThisDay(date: LocalDate): Boolean {
    return when (repeatedType) {
        RepeatedType.ONCE -> {
            startDate == date
        }

        RepeatedType.DAILY -> true
        RepeatedType.MONTHLY -> {
            days.contains(date.dayOfMonth)
        }

        RepeatedType.WEEKLY -> {
            days.contains(date.dayOfWeek.value)
        }

        RepeatedType.YEARLY -> {
            days.contains(date.dayOfYear)
        }
    }
}