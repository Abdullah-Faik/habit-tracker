package com.fola.habit_tracker.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fola.habit_tracker.HabitApplication
import com.fola.habit_tracker.data.data_base.Habit
import com.fola.habit_tracker.data.repositry.DataBaseHabitsRepository
import com.fola.habit_tracker.data.repositry.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

open class HomeViewModel(private val habitsRepository: HabitsRepository) : ViewModel() {

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    init {
        initializeHabits()
        getHabits()
    }

    private fun initializeHabits() {
        viewModelScope.launch {
            // Check if the database is empty
            val currentHabits = habitsRepository.getAllHabits(Habit(title = "Drink Water")).first()
            if (currentHabits.isEmpty()) {
                // Insert some test habits
                habitsRepository.getAllHabits(Habit(title = "Drink Water"))
                habitsRepository.getAllHabits(Habit(title = "Read Book"))
                habitsRepository.getAllHabits(Habit(title = "Exercise"))
            }
        }
    }

    fun addHabit(title: String) {
        viewModelScope.launch {
            habitsRepository.getAllHabits(Habit(title = title))
        }
    }

    private fun getHabits() {
        viewModelScope.launch {
            habitsRepository.getAllHabits(Habit(title = "Drink Water")).collect { habitsList ->
                println("Fetched habits: $habitsList")
                _habits.value = habitsList
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as HabitApplication)
                val repository: DataBaseHabitsRepository = application.habitsRepository
                HomeViewModel(repository)
            }
        }
    }
}