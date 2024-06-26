package com.askein.gymtracker.ui.workout.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRef
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.toExercise
import com.askein.gymtracker.ui.workout.WorkoutUiState
import com.askein.gymtracker.ui.workout.toWorkout
import kotlinx.coroutines.launch

class WorkoutExerciseCrossRefViewModel(
    private val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository
) : ViewModel() {
    fun saveExerciseToWorkout(exercise: ExerciseUiState, workout: WorkoutUiState) {
        viewModelScope.launch {
            workoutExerciseCrossRefRepository.saveExerciseToWorkout(
                exercise = exercise.toExercise(),
                workout = workout.toWorkout()
            )
        }
    }

    fun deleteExerciseFromWorkout(exercise: ExerciseUiState, workout: WorkoutUiState) {
        viewModelScope.launch {
            workoutExerciseCrossRefRepository.deleteExerciseFromWorkout(
                exercise = exercise.toExercise(),
                workout = workout.toWorkout()
            )
        }
    }

    fun updateExerciseOrderForWorkout(exercises: List<ExerciseUiState>, workout: WorkoutUiState) {
        viewModelScope.launch {
            workoutExerciseCrossRefRepository.updateOrderForWorkout(exerciseOrders = exercises.mapIndexed { index, exercise ->
                WorkoutExerciseCrossRef(
                    workoutId = workout.workoutId,
                    exerciseId = exercise.id,
                    order = index
                )
            })
        }
    }
}