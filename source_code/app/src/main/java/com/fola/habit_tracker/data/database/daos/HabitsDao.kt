package com.fola.habit_tracker.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fola.habit_tracker.data.database.Habit
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitsDao {
    @Query("SELECT * FROM habit")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE is_removed = 0")
    fun getActiveHabitsFlow(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE is_removed = 0 and repeated_type != 'ONCE'")
    suspend fun getActiveHabits(): List<Habit>

    @Query("select * FROM habit Where habit.habit_id =(:id)")
    suspend fun getHabit(id: Long): Habit?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHabit(habit: Habit)

    @Query("update habit set is_removed = 1 where habit_id =:habitId")
    suspend fun removeHabit(habitId: Long)

}