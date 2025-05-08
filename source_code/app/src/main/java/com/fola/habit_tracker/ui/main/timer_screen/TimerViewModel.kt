package com.fola.habit_tracker.ui.main.timer_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TimerViewModel : ViewModel() {
    private var _totalDuration = TimeUnit.MINUTES.toMillis(1)
    val totalDuration: Long
        get() = _totalDuration

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var countdownJob: Job? = null

    init {
        resetTimer()
    }

    fun setTotalDuration(durationMillis: Long) {
        _totalDuration = durationMillis.coerceAtLeast(0)
        resetTimer()
    }

    fun startTimer() {
        if (_timerState.value.isRunning || _timerState.value.remainingTime <= 0) return

        _timerState.value = _timerState.value.copy(isRunning = true)

        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val endTime = startTime + _timerState.value.remainingTime

            while (System.currentTimeMillis() < endTime) {
                if (!_timerState.value.isRunning) continue

                val currentTime = System.currentTimeMillis()
                val remaining = endTime - currentTime

                _timerState.value = _timerState.value.copy(
                    remainingTime = remaining,
                    progress = (_totalDuration - remaining).toFloat() / _totalDuration
                )

                kotlinx.coroutines.delay(16)
            }

            _timerState.value = _timerState.value.copy(
                isRunning = false,
                remainingTime = 0,
                progress = 1f
            )
        }
    }

    fun pauseTimer() {
        _timerState.value = _timerState.value.copy(isRunning = false)
    }

    fun resumeTimer() {
        if (!_timerState.value.isRunning && _timerState.value.remainingTime > 0) {
            startTimer()
        }
    }

    fun restartTimer() {
        stopTimer()
        resetTimer()
        startTimer()
    }

    fun stopTimer() {
        countdownJob?.cancel()
        _timerState.value = _timerState.value.copy(
            isRunning = false,
            remainingTime = 0,
            progress = 0f
        )
    }

    fun resetTimer() {
        _timerState.value = TimerState(
            remainingTime = _totalDuration,
            progress = 0f,
            isRunning = false,
            totalDuration = _totalDuration
        )
    }

    data class TimerState(
        val remainingTime: Long = 0,
        val progress: Float = 0f,
        val isRunning: Boolean = false,
        val totalDuration: Long = 0
    )
}