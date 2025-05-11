package com.fola.habit_tracker.data.repositry

import android.util.Log
import com.fola.habit_tracker.data.database.DailyHabits
import com.fola.habit_tracker.data.database.Day
import com.fola.habit_tracker.data.database.DayWithHabits
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.data.database.HabitDataBase
import com.fola.habit_tracker.data.database.daos.DailyHabitsDao
import com.fola.habit_tracker.data.database.daos.DayDao
import com.fola.habit_tracker.data.database.daos.HabitsDao
import com.fola.habit_tracker.data.database.isOnThisDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import kotlin.math.roundToInt

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

    suspend fun addNewHabit(habit: Habit)


    fun getDailyHabitProgress(dayId: LocalDate, habitId: Long): Flow<Int>

    suspend fun getDailyHabitProgressInt(dayId: LocalDate, habitId: Long): Int

    fun getDayProgress(dayId: LocalDate): Flow<Float>
    suspend fun getDay(dayId: LocalDate): Day?
    fun updateProgress(dayId: LocalDate, habitId: Long, value: Int)
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

    suspend fun getActiveHabit(): List<Habit> {
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
        dailyHabitsDao.insertDailyHabit(DailyHabits(id = habit.id))
    }

    override suspend fun addNewHabit(habit: Habit) {
        habitDao.insertHabit(habit)

        if (habit.startDate == LocalDate.now() && habit.isOnThisDay(LocalDate.now()))
            addNewDailyHabit(habit)
    }

    override fun getDailyHabitProgress(dayId: LocalDate, habitId: Long): Flow<Int> {
        return flow {
            emitAll(dailyHabitsDao.getDailyHabitProgress(dayId, habitId))
        }
    }

    override suspend fun getDailyHabitProgressInt(dayId: LocalDate, habitId: Long): Int {
        return dailyHabitsDao.getDailyHabitProgressInt(dayId, habitId)
    }

    override suspend fun initNewDay(dayId: LocalDate) {
        daysDao.insertDay(Day(dayId))
        val allHabit = habitDao.getActiveHabits().filter { it.isOnThisDay(dayId) }
        val dailyHabits = allHabit.map { habit ->
            DailyHabits(dayId = dayId, id = habit.id)
        }
        dailyHabitsDao.insertDailyHabits(dailyHabits)
    }

    override suspend fun deleteHabit(dayId: LocalDate, habitId: Long) {
        dailyHabitsDao.deleteDailyHabit(dayId, habitId)
        habitDao.removeHabit(habitId)
    }

    override fun getDayProgress(dayId: LocalDate): Flow<Float> {
        return daysDao.getDayProgress(dayId)
    }

    override suspend fun getDay(dayId: LocalDate): Day? {
        return daysDao.getDay(dayId)
    }

    override fun updateProgress(dayId: LocalDate, habitId: Long, value: Int) {
        dailyHabitsDao.setDailyHabitProgress(dayId, habitId, value)
    }


}