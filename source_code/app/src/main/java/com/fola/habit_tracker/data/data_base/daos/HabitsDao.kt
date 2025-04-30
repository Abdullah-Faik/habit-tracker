package com.fola.habit_tracker.data.data_base.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.fola.habit_tracker.data.data_base.Habit
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitsDao {
    @Query("SELECT * FROM habit")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE is_removed = 0")
    fun getActiveHabitsFlow(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE is_removed = 0 and repeated_type != 'TASK'")
    suspend fun getActiveHabits(): List<Habit>

    @Query("select * FROM habit Where habit.habit_id =(:id)")
    suspend fun getHabit(id: Long): Habit?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHabit(habit: Habit)

    @Query("update habit set is_removed = 1 where habit_id =:habitId")
    suspend fun removeHabit(habitId: Long)

}