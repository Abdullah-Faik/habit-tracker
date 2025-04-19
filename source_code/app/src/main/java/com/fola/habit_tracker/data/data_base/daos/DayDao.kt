package com.fola.habit_tracker.data.data_base.daos

import androidx.privacysandbox.ads.adservices.adid.AdId
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.fola.habit_tracker.data.data_base.Day
import com.fola.habit_tracker.data.data_base.DayWithHabits


@Dao
interface DayDao {

    @Upsert
    suspend fun upsert(day: Day)

    @Query("SELECT progress from day where Day.dayId = :date")
    suspend fun getProgress(date: String): Float = 0f

    @Query("UPDATE day set progress  = :progress where dayId = :id")
    suspend fun upDateProgress(progress: Float, id: String)


    @Transaction
    @Query("SELECT * FROM day where dayId = :dayId")
    suspend fun getHabits(dayId: String) : DayWithHabits

}