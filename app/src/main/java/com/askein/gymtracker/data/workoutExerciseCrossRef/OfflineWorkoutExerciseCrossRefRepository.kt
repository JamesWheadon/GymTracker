package com.askein.gymtracker.data.workoutExerciseCrossRef

import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.workout.Workout

class OfflineWorkoutExerciseCrossRefRepository(private val workoutExerciseDao: WorkoutExerciseCrossRefDao): WorkoutExerciseCrossRefRepository {
    override suspend fun saveExerciseToWorkout(exercise: Exercise, workout: Workout) = workoutExerciseDao.insert(
        WorkoutExerciseCrossRef(workoutId = workout.workoutId, exerciseId = exercise.exerciseId)
    )

    override suspend fun deleteExerciseFromWorkout(exercise: Exercise, workout: Workout) = workoutExerciseDao.delete(
        WorkoutExerciseCrossRef(workoutId = workout.workoutId, exerciseId = exercise.exerciseId)
    )

    override suspend fun deleteAllCrossRefForWorkout(workout: Workout) = workoutExerciseDao.deleteAllCrossRefForWorkout(workout.workoutId)

    override suspend fun deleteAllCrossRefForExercise(exercise: Exercise) = workoutExerciseDao.deleteAllCrossRefForExercise(exercise.exerciseId)
}
