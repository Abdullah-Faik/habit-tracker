package com.fola.habit_tracker.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fola.habit_tracker.data.database.DailyHabits
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


@Dao
interface DailyHabitsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDailyHabit(dailyHabit: DailyHabits)


    @Insert
    suspend fun insertDailyHabits(dailyHabit: List<DailyHabits>)

    @Query("SELECT * FROM daily_habit WHERE day_id = :dayId AND habit_id = :habitId")
    suspend fun getDailyHabit(dayId: LocalDate, habitId: Long): DailyHabits?

    @Query("select * FROM daily_habit where day_id = :dayId")
    fun getDailyHabits(dayId: LocalDate): Flow<List<DailyHabits>>

    @Query("select progress from daily_habit where day_id = :dayId and habit_id = :habitId")
    fun getDailyHabitProgress(dayId: LocalDate, habitId: Long) : Flow<Int>

    @Query("select progress from daily_habit where day_id = :dayId and habit_id = :habitId")
    fun getDailyHabitProgressInt(dayId: LocalDate, habitId: Long) : Int



    @Query("delete from  daily_habit where day_id =:dayId and habit_id = :habitId")
    fun deleteDailyHabit(dayId: LocalDate, habitId: Long)


    @Query("UPDATE daily_habit set progress = :value where day_id = :dayId and habit_id = :habitId")
    fun setDailyHabitProgress(dayId: LocalDate , habitId: Long, value : Int)
}