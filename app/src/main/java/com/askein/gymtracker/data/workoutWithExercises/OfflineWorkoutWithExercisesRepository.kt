package com.askein.gymtracker.data.workoutWithExercises

import kotlinx.coroutines.flow.Flow

class OfflineWorkoutWithExercisesRepository(private val workoutWithExercisesDao: WorkoutWithExercisesDao): WorkoutWithExercisesRepository {
    override fun getWorkoutWithExercisesStream(workoutId: Int): Flow<WorkoutWithExercises> = workoutWithExercisesDao.getWorkoutWithExercises(workoutId)
}