package com.askein.gymtracker.data.workout

import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllWorkoutsStream(): Flow<List<Workout>>

    suspend fun insertWorkout(workout: Workout)

    suspend fun updateWorkout(workout: Workout)

    suspend fun deleteWorkout(workout: Workout)
}
