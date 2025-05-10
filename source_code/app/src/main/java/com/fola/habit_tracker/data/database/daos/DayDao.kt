package com.fola.habit_tracker.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.fola.habit_tracker.data.database.Day
import com.fola.habit_tracker.data.database.DayWithHabits
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDay(day: Day)


    @Query("SELECT * FROM day where day_id = :id")
    suspend fun getDay(id: LocalDate): Day?

    @Transaction
    @Query("SELECT * FROM day where day_id = :id")
    fun getHabits(id: LocalDate): Flow<DayWithHabits>

}