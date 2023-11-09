package com.example.gymtracker.data.workout

import kotlinx.coroutines.flow.Flow

class OfflineWorkoutRepository(private val workoutDao: WorkoutDao): WorkoutRepository {
    override fun getAllWorkoutsStream(): Flow<List<Workout>> = workoutDao.getAllWorkouts()

    override suspend fun insertWorkout(workout: Workout) = workoutDao.insert(workout)
}
