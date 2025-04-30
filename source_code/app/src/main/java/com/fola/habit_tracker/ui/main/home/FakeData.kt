package com.fola.habit_tracker.ui.main.home

import com.fola.habit_tracker.data.data_base.Habit
import com.fola.habit_tracker.data.data_base.RepeatedType
import com.fola.habit_tracker.data.repositry.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalTime


class FakeHabitsRepository : HabitsRepository {
    override suspend fun getAllHabits(habit: Habit): Flow<List<Habit>> {
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

}


val fakeViewModel = HomeViewModel(habitsRepository = FakeHabitsRepository())
