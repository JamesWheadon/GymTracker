package com.askein.gymtracker.data.workoutExerciseCrossRef

import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.workout.Workout

interface WorkoutExerciseCrossRefRepository {
    suspend fun saveExerciseToWorkout(exercise: Exercise, workout: Workout)

    suspend fun saveExercisesToWorkout(exercises: List<Exercise>, workout: Workout)

    suspend fun deleteExerciseFromWorkout(exercise: Exercise, workout: Workout)

    suspend fun deleteExercisesFromWorkout(exercises: List<Exercise>, workout: Workout)

    suspend fun updateOrderForWorkout(exerciseOrders: List<WorkoutExerciseCrossRef>)

    suspend fun deleteAllCrossRefForWorkout(workout: Workout)

    suspend fun deleteAllCrossRefForExercise(exercise: Exercise)
}
