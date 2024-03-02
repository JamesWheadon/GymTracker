package com.example.gymtracker.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.workout.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutScreenViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val workoutListUiState: StateFlow<WorkoutListUiState> =
        workoutRepository.getAllWorkoutsStream()
            .map { workoutList -> WorkoutListUiState(workoutList.map { it.toWorkoutUiState() }) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WorkoutListUiState()
            )

    fun saveWorkout(workout: WorkoutUiState) {
        viewModelScope.launch {
            workoutRepository.insertWorkout(workout.toWorkout())
        }
    }
}
