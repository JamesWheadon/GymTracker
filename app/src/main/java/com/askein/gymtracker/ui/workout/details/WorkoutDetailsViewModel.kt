package com.askein.gymtracker.ui.workout.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askein.gymtracker.data.workout.WorkoutRepository
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.askein.gymtracker.data.workoutWithExercises.WorkoutWithExercisesRepository
import com.askein.gymtracker.ui.workout.WorkoutUiState
import com.askein.gymtracker.ui.workout.toWorkout
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
    val chosenDate: String = checkNotNull(savedStateHandle["chosenDate"])

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

    fun updateWorkout(workout: WorkoutUiState) {
        viewModelScope.launch {
            workoutRepository.updateWorkout(workout.toWorkout())
        }
    }

    fun deleteWorkout(workout: WorkoutUiState) {
        viewModelScope.launch {
            workoutExerciseCrossRefRepository.deleteAllCrossRefForWorkout(workout.toWorkout())
            workoutRepository.deleteWorkout(workout.toWorkout())
        }
    }
}
