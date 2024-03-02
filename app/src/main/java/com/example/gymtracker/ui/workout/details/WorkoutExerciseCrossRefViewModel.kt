package com.example.gymtracker.ui.workout.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.toExercise
import com.example.gymtracker.ui.workout.WorkoutUiState
import com.example.gymtracker.ui.workout.toWorkout
import kotlinx.coroutines.launch

class WorkoutExerciseCrossRefViewModel(
    private val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository
) : ViewModel() {
    fun saveExerciseToWorkout(exercise: ExerciseUiState, workout: WorkoutUiState) {
        viewModelScope.launch {
            workoutExerciseCrossRefRepository.saveExerciseToWorkout(exercise.toExercise(), workout.toWorkout())
        }
    }

    fun deleteExerciseFromWorkout(exercise: ExerciseUiState, workout: WorkoutUiState) {
        viewModelScope.launch {
            workoutExerciseCrossRefRepository.deleteExerciseFromWorkout(exercise.toExercise(), workout.toWorkout())
        }
    }
}