package com.fola.habit_tracker

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.fola.habit_tracker.data.data_base.HabitDataBase
import com.fola.habit_tracker.data.repositry.DataBaseHabitsRepository

class HabitApplication : Application() {
    private val db: HabitDataBase by lazy {
        val database = HabitDataBaseProvider.getDatabase(this)
        database
    }

    val habitsRepository: DataBaseHabitsRepository by lazy {
        val repo = DataBaseHabitsRepository(db)
        repo
    }
}

object HabitDataBaseProvider {

    @Volatile
    private lateinit var db: HabitDataBase


    fun getDatabase(context: Context): HabitDataBase {
        if (!::db.isInitialized) {
            synchronized(this) {
                if (!::db.isInitialized) {
                    db = Room.databaseBuilder(
                        context.applicationContext,
                        HabitDataBase::class.java,
                        "habit_database"
                    ).build()
                }
            }
        }
        return db
    }
}