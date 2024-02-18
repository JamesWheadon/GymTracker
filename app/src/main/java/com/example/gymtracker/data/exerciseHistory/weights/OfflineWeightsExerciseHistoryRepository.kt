package com.example.gymtracker.data.exerciseHistory.weights

import com.example.gymtracker.data.exercise.Exercise
import kotlinx.coroutines.flow.Flow

class OfflineWeightsExerciseHistoryRepository(private val weightsExerciseHistoryDao: WeightsExerciseHistoryDao) :
    WeightsExerciseHistoryRepository {
    override fun getHistoryStream(id: Int): Flow<WeightsExerciseHistory?> = weightsExerciseHistoryDao.getHistory(id)

    override fun getFullExerciseHistoryStream(exerciseId: Int): Flow<List<WeightsExerciseHistory>?> =
        weightsExerciseHistoryDao.getFullExerciseHistory(exerciseId)

    override fun getLatestHistoryStream(id: Int): Flow<WeightsExerciseHistory?> =
        weightsExerciseHistoryDao.getLatestExerciseHistory(id)

    override fun getRecentHistoryStream(id: Int, days: Int): Flow<List<WeightsExerciseHistory>?> =
        weightsExerciseHistoryDao.getRecentExerciseHistory(id, days)

    override suspend fun insertHistory(history: WeightsExerciseHistory) = weightsExerciseHistoryDao.insert(history)

    override suspend fun update(history: WeightsExerciseHistory) = weightsExerciseHistoryDao.update(history)

    override suspend fun delete(history: WeightsExerciseHistory) = weightsExerciseHistoryDao.delete(history)

    override suspend fun deleteAllForExercise(exercise: Exercise) = weightsExerciseHistoryDao.deleteAllForExercise(exercise.exerciseId)
}
