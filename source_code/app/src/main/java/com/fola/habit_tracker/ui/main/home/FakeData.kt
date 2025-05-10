package com.fola.habit_tracker.ui.main.home
import com.fola.habit_tracker.data.repositry.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalTime


class FakeHabitsRepository : HabitsRepository {

    override suspend fun deleteHabit(dayId: LocalDate, habitId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun addNewHabit(habit: Habit) {
        TODO("Not yet implemented")
    }

    override fun getDailyHabitProgress(dayId: LocalDate, habitId: Long): Flow<Float> {
        TODO("Not yet implemented")
    }

}

val fakeViewModel = HomeViewModel(habitsRepository = FakeHabitsRepository())
