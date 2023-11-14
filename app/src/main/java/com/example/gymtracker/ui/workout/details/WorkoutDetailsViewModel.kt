package com.example.gymtracker.ui.workout.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workout.WorkoutRepository
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercisesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutDetailsViewModel(
    private val workoutRepository: WorkoutRepository,
    workoutWithExercisesRepository: WorkoutWithExercisesRepository,
    private val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val workoutId: Int = checkNotNull(savedStateHandle["workoutId"])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val uiState: StateFlow<WorkoutWithExercisesUiState> =
        workoutWithExercisesRepository.getWorkoutWithExercisesStream(workoutId)
            .filterNotNull()
            .map { workout -> workout.toWorkoutWithExercisesUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WorkoutWithExercisesUiState()
            )

    fun updateWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutRepository.updateWorkout(workout)
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutExerciseCrossRefRepository.deleteAllCrossRefForWorkout(workout)
            workoutRepository.deleteWorkout(workout)
        }
    }
}
