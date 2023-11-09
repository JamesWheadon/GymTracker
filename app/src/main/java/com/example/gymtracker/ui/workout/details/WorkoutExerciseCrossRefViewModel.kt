package com.example.gymtracker.ui.workout.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import kotlinx.coroutines.launch

class WorkoutExerciseCrossRefViewModel(
    private val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository
) : ViewModel() {
    fun saveExerciseToWorkout(exercise: Exercise, workout: Workout) {
        viewModelScope.launch {
            workoutExerciseCrossRefRepository.saveExerciseToWorkout(exercise, workout)
        }
    }

    fun deleteExerciseFromWorkout(exercise: Exercise, workout: Workout) {
        viewModelScope.launch {
            workoutExerciseCrossRefRepository.deleteExerciseFromWorkout(exercise, workout)
        }
    }
}