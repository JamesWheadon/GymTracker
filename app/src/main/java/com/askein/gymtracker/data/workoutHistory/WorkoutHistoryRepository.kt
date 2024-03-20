package com.askein.gymtracker.data.workoutHistory

interface WorkoutHistoryRepository {
    suspend fun insert(workoutHistory: WorkoutHistory): Long

    suspend fun update(workoutHistory: WorkoutHistory)

    suspend fun delete(workoutHistory: WorkoutHistory)
}
