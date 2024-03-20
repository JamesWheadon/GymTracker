package com.askein.gymtracker.data.workoutExerciseCrossRef

import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.workout.Workout

interface WorkoutExerciseCrossRefRepository {
    suspend fun saveExerciseToWorkout(exercise: Exercise, workout: Workout)

    suspend fun deleteExerciseFromWorkout(exercise: Exercise, workout: Workout)

    suspend fun deleteAllCrossRefForWorkout(workout: Workout)

    suspend fun deleteAllCrossRefForExercise(exercise: Exercise)
}
