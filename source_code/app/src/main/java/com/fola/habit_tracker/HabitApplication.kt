package com.fola.habit_tracker

import android.app.Application
import androidx.room.Room
import com.fola.habit_tracker.data.data_base.HabitDataBase
import com.fola.habit_tracker.data.repositry.DataBaseHabitsRepository

class HabitApplication : Application() {

    val db: HabitDataBase by lazy {
        Room.databaseBuilder(
            applicationContext,
            HabitDataBase::class.java,
            "HabitDataBase"
        ).build()

    }

    val habitsRepository: DataBaseHabitsRepository by lazy {
        DataBaseHabitsRepository(db)
    }

    override fun onCreate() {
        super.onCreate()
    }

}