package com.fola.habit_tracker.data.repositry

import androidx.media3.common.util.Log
import com.fola.habit_tracker.data.data_base.DailyHabits
import com.fola.habit_tracker.data.data_base.Day
import com.fola.habit_tracker.data.data_base.DayWithHabits
import com.fola.habit_tracker.data.data_base.Habit
import com.fola.habit_tracker.data.data_base.HabitDataBase
import com.fola.habit_tracker.data.data_base.daos.DailyHabitsDao
import com.fola.habit_tracker.data.data_base.daos.DayDao
import com.fola.habit_tracker.data.data_base.daos.HabitsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

interface HabitsRepository {

    //get habits
    suspend fun getActiveHabitsFlow(): Flow<List<Habit>>
    fun getDailyHabits(dayId: LocalDate): Flow<DayWithHabits>

    //add new habits
    suspend fun addNewDailyHabit(habit: Habit)

    //init new day with the habits
    suspend fun initNewDay(dayId: LocalDate)

    //delete habit
    suspend fun deleteHabit(dayId: LocalDate, habitId: Long)

    //getday

}


class DataBaseHabitsRepository(
    db: HabitDataBase
) : HabitsRepository {

    private val dailyHabitsDao: DailyHabitsDao = db.dailyHabitsDao()
    private val daysDao: DayDao = db.daoDao()
    private val habitDao: HabitsDao = db.habitDao()

    override suspend fun getActiveHabitsFlow(): Flow<List<Habit>> {
        return habitDao.getActiveHabitsFlow()
    }

    private suspend fun getActiveHabit(): List<Habit> {
        return habitDao.getActiveHabits()
    }

    override fun getDailyHabits(dayId: LocalDate): Flow<DayWithHabits> {
        return flow {
            if (daysDao.getDay(dayId) != null) {
                emitAll(daysDao.getHabits(dayId))
            } else if (dayId >= LocalDate.now()) {
                emit(DayWithHabits(Day(dayId), getActiveHabit()))
            } else {
                emit(DayWithHabits(Day(dayId), emptyList()))
            }
        }
    }

    override suspend fun addNewDailyHabit(habit: Habit) {
        habitDao.insertHabit(habit)
        dailyHabitsDao.insertDailyHabit(DailyHabits(id = habit.id))
    }

    override suspend fun initNewDay(dayId: LocalDate) {
        if (daysDao.getDay(dayId) != null)
            return
        else {
            daysDao.insertDay(Day(dayId))
        }
    }

    override suspend fun deleteHabit(dayId: LocalDate, habitId: Long) {
        dailyHabitsDao.deleteDailyHabit(dayId, habitId)
        Log.d("clicking", "clicked")
        habitDao.removeHabit(habitId)
    }


}