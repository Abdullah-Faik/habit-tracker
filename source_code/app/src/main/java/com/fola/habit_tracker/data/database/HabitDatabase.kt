package com.fola.habit_tracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fola.habit_tracker.data.database.daos.DailyHabitsDao
import com.fola.habit_tracker.data.database.daos.DayDao
import com.fola.habit_tracker.data.database.daos.HabitsDao


@Database(entities = [Day::class, Habit::class, DailyHabits::class], version = 2)
@TypeConverters(Converters::class)
abstract class HabitDataBase : RoomDatabase() {

    abstract fun habitDao() : HabitsDao
    abstract fun dailyHabitsDao() : DailyHabitsDao
    abstract fun daoDao() : DayDao
}