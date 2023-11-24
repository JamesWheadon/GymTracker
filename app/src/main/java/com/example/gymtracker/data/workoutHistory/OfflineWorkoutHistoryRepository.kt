package com.example.gymtracker.data.workoutHistory

class OfflineWorkoutHistoryRepository(private val workoutHistoryDao: WorkoutHistoryDao) : WorkoutHistoryRepository {
    override suspend fun insert(workoutHistory: WorkoutHistory): Long = workoutHistoryDao.insert(workoutHistory)
}
