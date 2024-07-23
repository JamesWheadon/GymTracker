package com.askein.gymtracker.ui.workout.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.toExercise
import kotlinx.coroutines.launch

class WorkoutExerciseCrossRefViewModel(
    private val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository
) : ViewModel() {

    fun saveExercisesForWorkout(
        exercises: List<ExerciseUiState>,
        workout: WorkoutWithExercisesUiState
    ) {
        val removeExercises = workout.exercises.toSet().minus(exercises.toSet())
        viewModelScope.launch {
            if (removeExercises.isNotEmpty()) {
                workoutExerciseCrossRefRepository.deleteExercisesFromWorkout(
                    exercises = removeExercises.map { exercise -> exercise.toExercise() }.toList(),
                    workout = workout.toWorkout()
                )
            }
            workoutExerciseCrossRefRepository.saveExercisesToWorkout(
                exercises = exercises.map { exercise -> exercise.toExercise() },
                workout = workout.toWorkout()
            )
        }
    }
}
