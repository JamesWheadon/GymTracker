package com.example.gymtracker.data.workoutExerciseCrossRef

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.workout.Workout

interface WorkoutExerciseCrossRefRepository {
    suspend fun saveExerciseToWorkout(exercise: Exercise, workout: Workout)

    suspend fun deleteExerciseFromWorkout(exercise: Exercise, workout: Workout)
}
