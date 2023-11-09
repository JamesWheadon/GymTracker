package com.example.gymtracker.data.workoutExerciseCrossRef

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.workout.Workout

class OfflineWorkoutExerciseCrossRefRepository(private val workoutExerciseDao: WorkoutExerciseCrossRefDao): WorkoutExerciseCrossRefRepository {
    override suspend fun saveExerciseToWorkout(exercise: Exercise, workout: Workout) = workoutExerciseDao.insert(
        WorkoutExerciseCrossRef(workoutId = workout.workoutId, exerciseId = exercise.exerciseId)
    )

    override suspend fun deleteExerciseFromWorkout(exercise: Exercise, workout: Workout) = workoutExerciseDao.delete(
        WorkoutExerciseCrossRef(workoutId = workout.workoutId, exerciseId = exercise.exerciseId)
    )

    override suspend fun deleteAllCrossRefForWorkout(workout: Workout) = workoutExerciseDao.deleteAllCrossRefForWorkout(workout.workoutId)
}
