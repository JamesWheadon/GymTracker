package com.example.gymtracker.ui.workout.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercisesRepository
import com.example.gymtracker.ui.workout.WorkoutWithExercisesUiState
import com.example.gymtracker.ui.workout.toWorkoutWithExercisesUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WorkoutDetailsViewModel(
    workoutWithExercisesRepository: WorkoutWithExercisesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val workoutId: Int = checkNotNull(savedStateHandle["workoutId"])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val uiState: StateFlow<WorkoutWithExercisesUiState> =
        workoutWithExercisesRepository.getWorkoutWithExercisesStream(workoutId)
            .map { workout -> workout.toWorkoutWithExercisesUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WorkoutWithExercisesUiState()
            )
}
