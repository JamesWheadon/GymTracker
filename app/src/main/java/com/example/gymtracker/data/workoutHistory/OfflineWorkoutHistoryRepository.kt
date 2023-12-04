package com.example.gymtracker.data.workoutHistory

class OfflineWorkoutHistoryRepository(private val workoutHistoryDao: WorkoutHistoryDao) : WorkoutHistoryRepository {
    override suspend fun insert(workoutHistory: WorkoutHistory): Long = workoutHistoryDao.insert(workoutHistory)

    override suspend fun update(workoutHistory: WorkoutHistory) = workoutHistoryDao.update(workoutHistory)

    override suspend fun delete(workoutHistory: WorkoutHistory) = workoutHistoryDao.delete(workoutHistory)
}
