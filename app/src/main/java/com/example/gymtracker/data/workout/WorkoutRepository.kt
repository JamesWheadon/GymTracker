package com.example.gymtracker.data.workout

import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllWorkoutsStream(): Flow<List<Workout>>

    suspend fun insertWorkout(workout: Workout)
}
