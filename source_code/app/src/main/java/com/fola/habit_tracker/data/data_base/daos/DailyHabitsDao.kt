package com.fola.habit_tracker.data.data_base.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fola.habit_tracker.data.data_base.DailyHabits


@Dao
interface DailyHabitsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyHabit(dailyHabits: DailyHabits)

    @Query("UPDATE daily_habit SET isDone = :isDone WHERE dayId = :dayId AND id = :habitId")
    suspend fun updateHabitCompletedSate(dayId: String, habitId: Int, isDone: Boolean = true)

    @Query("SELECT * FROM daily_habit WHERE dayId = :dayId AND id = :habitId")
    suspend fun getDailyHabit(dayId: String, habitId: Int): DailyHabits?

}