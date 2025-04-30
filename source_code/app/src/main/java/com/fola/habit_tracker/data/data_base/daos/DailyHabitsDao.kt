package com.fola.habit_tracker.data.data_base.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fola.habit_tracker.data.data_base.DailyHabits
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate


@Dao
interface DailyHabitsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDailyHabit(dailyHabit: DailyHabits)

    @Query("SELECT * FROM daily_habit WHERE day_id = :dayId AND habit_id = :habitId")
    suspend fun getDailyHabit(dayId: LocalDate, habitId: Long): DailyHabits?

    @Query("select * FROM daily_habit where day_id = :dayId")
    fun getDailyHabits(dayId: LocalDate): Flow<List<DailyHabits>>


    @Query("delete from  daily_habit where day_id =:dayId and habit_id = :habitId")
    fun deleteDailyHabit(dayId: LocalDate, habitId: Long)
}