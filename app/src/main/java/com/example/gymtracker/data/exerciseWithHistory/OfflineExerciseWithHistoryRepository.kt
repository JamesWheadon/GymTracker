package com.example.gymtracker.data.exerciseWithHistory

import kotlinx.coroutines.flow.Flow

class OfflineExerciseWithHistoryRepository(private val exerciseWithHistoryDao: ExerciseWithHistoryDao): ExerciseWithHistoryRepository {
    override fun getExerciseWithHistoryStream(exerciseId: Int): Flow<ExerciseWithHistory> = exerciseWithHistoryDao.getExerciseWithHistory(exerciseId)
}
