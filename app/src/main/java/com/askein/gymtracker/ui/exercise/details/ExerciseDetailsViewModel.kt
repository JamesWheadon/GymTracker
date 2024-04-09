package com.askein.gymtracker.ui.exercise.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askein.gymtracker.data.exercise.ExerciseRepository
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.askein.gymtracker.data.exerciseWithHistory.ExerciseWithHistoryRepository
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.toExercise
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciseDetailsViewModel(
    private val exerciseRepository: ExerciseRepository,
    private val weightsExerciseHistoryRepository: WeightsExerciseHistoryRepository,
    private val cardioExerciseHistoryRepository: CardioExerciseHistoryRepository,
    private val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository,
    exerciseWithHistoryRepository: ExerciseWithHistoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exerciseId: Int = checkNotNull(savedStateHandle["exerciseId"])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val uiState: StateFlow<ExerciseDetailsUiState> =
        exerciseWithHistoryRepository.getExerciseWithHistoryStream(exerciseId)
            .filterNotNull()
            .map { exercise -> exercise.toExerciseDetailsUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ExerciseDetailsUiState()
            )

    fun updateExercise(exercise: ExerciseUiState) {
        viewModelScope.launch {
            exerciseRepository.updateExercise(exercise.toExercise())
        }
    }

    fun deleteExercise(exerciseUiState: ExerciseUiState) {
        val exercise = exerciseUiState.toExercise()
        viewModelScope.launch {
            if (exercise.equipment != "") {
                weightsExerciseHistoryRepository.deleteAllForExercise(exercise)
            } else {
                cardioExerciseHistoryRepository.deleteAllForExercise(exercise)
            }
            workoutExerciseCrossRefRepository.deleteAllCrossRefForExercise(exercise)
            exerciseRepository.deleteExercise(exercise)
        }
    }
}
