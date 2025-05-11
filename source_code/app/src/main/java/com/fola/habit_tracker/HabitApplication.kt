package com.fola.habit_tracker

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.fola.habit_tracker.data.database.HabitDataBase
import com.fola.habit_tracker.data.repositry.DataBaseHabitsRepository
import com.fola.habit_tracker.ui.main.profileScreen.LocalProfileRepository

class HabitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("HabitApplication", "onCreate: Initializing with context=$this")
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("AppCrash", "Uncaught exception", throwable)
            System.exit(1)
        }
        // Initialize profile data
        LocalProfileRepository.loadProfileImageUri(this)
        Log.i("HabitApplication", "onCreate: Profile image URI loaded")
    }

    private val db: HabitDataBase by lazy {
        val database = HabitDataBaseProvider.getDatabase(this)
        database
    }
    val habitsRepository: DataBaseHabitsRepository by lazy {
        DataBaseHabitsRepository(db)
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