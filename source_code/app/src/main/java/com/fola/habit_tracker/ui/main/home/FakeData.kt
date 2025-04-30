package com.fola.habit_tracker.ui.main.home

import com.fola.habit_tracker.data.data_base.DayWithHabits
import com.fola.habit_tracker.data.data_base.Habit
import com.fola.habit_tracker.data.data_base.RepeatedType
import com.fola.habit_tracker.data.repositry.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalTime


class FakeHabitsRepository : HabitsRepository {
    suspend fun getAllHabits(): Flow<List<Habit>> {
        return flowOf(
            listOf(
                Habit(id = 1, title = "Drink Water", repeatedType = RepeatedType.DAILY),
                Habit(id = 2, title = "Read Book", repeatedType = RepeatedType.DAILY),
                Habit(id = 3, title = "Exercise", reminderTime = LocalTime.now()),
                Habit(id = 3, title = "Exercise", reminderTime = LocalTime.now()),
                Habit(id = 3, title = "Exercise", reminderTime = LocalTime.now()),
                Habit(id = 3, title = "Exercise", reminderTime = LocalTime.now()),
                Habit(id = 3, title = "Exercise", reminderTime = LocalTime.now()),
                )
        )
    }

    override suspend fun getActiveHabitsFlow(): Flow<List<Habit>> {
        TODO("Not yet implemented")
    }

    override fun getDailyHabits(dayId: LocalDate): Flow<DayWithHabits> {
        TODO("Not yet implemented")
    }

    override suspend fun addNewDailyHabit(habit: Habit) {
        TODO("Not yet implemented")
    }

    override suspend fun initNewDay(dayId: LocalDate) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHabit(dayId: LocalDate, habitId: Long) {
        TODO("Not yet implemented")
    }

}


val fakeViewModel = HomeViewModel(habitsRepository = FakeHabitsRepository())
