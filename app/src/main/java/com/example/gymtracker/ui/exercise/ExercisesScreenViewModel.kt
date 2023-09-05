package com.example.gymtracker.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExercisesScreenViewModel(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val exerciseListUiState: StateFlow<ExerciseListUiState> =
        exerciseRepository.getAllExercisesStream().map { ExerciseListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ExerciseListUiState()
            )

    fun saveExercise(newExercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.insertExercise(newExercise)
        }
    }
}
