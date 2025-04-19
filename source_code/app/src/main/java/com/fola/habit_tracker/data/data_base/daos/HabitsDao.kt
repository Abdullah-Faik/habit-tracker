package com.fola.habit_tracker.data.data_base.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.fola.habit_tracker.data.data_base.Habit
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitsDao {
    @Query("SELECT * FROM habit")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE is_removed != 0")
    suspend fun getActiveHabits(): List<Habit>

    @Query("select * FROM habit Where habit.id =(:id)")
    suspend fun getHabit(id: Int) : Habit?

    @Upsert()
    suspend fun upsert(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("DELETE FROM habit")
    suspend fun deleteAll()

}