package com.askein.gymtracker.ui.workout.history.create.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LiveRecordWeightsExerciseViewModel : ViewModel() {

    val exerciseState = MutableStateFlow(WeightsExerciseHistoryUiState())
    val timerState = MutableStateFlow(TimerState())
    val completed = MutableStateFlow(false)
    private var timerJob: Job? = null

    fun setExerciseData(exerciseId: Int, reps: Int, rest: Int, weight: Double) {
        exerciseState.tryEmit(
            WeightsExerciseHistoryUiState(
                exerciseId = exerciseId,
                reps = reps,
                rest = rest,
                weight = weight
            )
        )
    }

    fun finishSet() {
        exerciseState.tryEmit(exerciseState.value.copy(sets = exerciseState.value.sets + 1))
    }

    fun startTimer(rest: Int) {
        timerState.tryEmit(timerState.value.copy(currentTime = rest, timerRunning = true))
        timerJob = viewModelScope.launch {
            while (timerState.value.currentTime > 0) {
                withContext(Dispatchers.Main) {
                    timerState.tryEmit(timerState.value.copy(currentTime = timerState.value.currentTime - 1))
                }
                delay(1000)
            }
            timerState.tryEmit(timerState.value.copy(timerRunning = false))
            completed.tryEmit(true)
        }
    }

    fun reset() {
        timerJob?.cancel()
        timerState.tryEmit(timerState.value.copy(timerRunning = false))
        completed.tryEmit(false)
    }
}
