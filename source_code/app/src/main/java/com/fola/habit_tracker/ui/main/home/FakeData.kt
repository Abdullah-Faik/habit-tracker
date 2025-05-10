package com.fola.habit_tracker.ui.main.home

import com.fola.habit_tracker.data.database.Day
import com.fola.habit_tracker.data.database.DayWithHabits
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.data.database.RepeatedType
import com.fola.habit_tracker.data.repositry.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalTime


class FakeHabitsRepository : HabitsRepository {


    override suspend fun getActiveHabitsFlow(): Flow<List<Habit>> {
        TODO("Not yet implemented")
    }

    override fun getDailyHabits(dayId: LocalDate): Flow<DayWithHabits> {
        return flowOf(
            DayWithHabits(
                day = com.fola.habit_tracker.data.database.Day(LocalDate.now()),
                habits = listOf(
                    Habit(id = 1, title = "Drink Water", repeatedType = RepeatedType.DAILY),
                    Habit(id = 2, title = "Read Book", repeatedType = RepeatedType.DAILY),
                    Habit(id = 3, title = "Exercise", startTime = LocalTime.now()),
                    Habit(id = 4, title = "Meditate", startTime = LocalTime.now()),
                    Habit(id = 5, title = "Sleep Early", startTime = LocalTime.now())
                )
            )
        )    }

    override suspend fun addNewDailyHabit(habit: Habit) {
        TODO("Not yet implemented")
    }

    override suspend fun initNewDay(dayId: LocalDate) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHabit(dayId: LocalDate, habitId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun addNewHabit(habit: Habit) {
        TODO("Not yet implemented")
    }

    override fun getDailyHabitProgress(dayId: LocalDate, habitId: Long): Flow<Float> {
        TODO("Not yet implemented")
    }

    override fun getDayProgress(dayId: LocalDate): Flow<Float> {
        TODO("Not yet implemented")
    }

    override suspend fun getDay(dayId: LocalDate): Day? {
        TODO("Not yet implemented")
    }

}

val fakeViewModel = HomeViewModel(habitsRepository = FakeHabitsRepository())
