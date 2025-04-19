package com.fola.habit_tracker.data.repositry

import com.fola.habit_tracker.data.data_base.Habit
import com.fola.habit_tracker.data.data_base.HabitDataBase
import com.fola.habit_tracker.data.data_base.daos.DailyHabitsDao
import com.fola.habit_tracker.data.data_base.daos.DayDao
import com.fola.habit_tracker.data.data_base.daos.HabitsDao
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    suspend fun getAllHabits(): Flow<List<Habit>>
}


class DataBaseHabitsRepository(
    db: HabitDataBase
) : HabitsRepository {

    private val dailyHabitsDao: DailyHabitsDao = db.dailyHabitsDao()
    private val daysDao: DayDao = db.daoDao()
    private val habitDao: HabitsDao = db.habitDao()

    override suspend fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits()
    }

}