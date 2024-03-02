package com.example.gymtracker.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exercise.ExerciseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExercisesScreenViewModel(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val exerciseListUiState: StateFlow<ExerciseListUiState> =
        exerciseRepository.getAllExercisesStream()
            .map { exerciseList ->
                ExerciseListUiState(
                    exerciseList.map { exercise -> exercise.toExerciseUiState() }
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ExerciseListUiState()
            )

    val muscleGroupUiState: StateFlow<List<String>> =
        exerciseRepository.getAllMuscleGroupsStream()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = listOf()
            )

    val exerciseNamesUiState: StateFlow<List<String>> =
        exerciseRepository.getAllExerciseNames()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = listOf()
            )

    fun saveExercise(newExercise: ExerciseUiState) {
        viewModelScope.launch {
            exerciseRepository.insertExercise(newExercise.toExercise())
        }
    }
}
