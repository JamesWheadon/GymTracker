package com.askein.gymtracker.data.exerciseHistory.weights

import com.askein.gymtracker.data.exercise.Exercise
import kotlinx.coroutines.flow.Flow

class OfflineWeightsExerciseHistoryRepository(private val weightsExerciseHistoryDao: WeightsExerciseHistoryDao) :
    WeightsExerciseHistoryRepository {
    override fun getHistoryStream(id: Int): Flow<WeightsExerciseHistory?> = weightsExerciseHistoryDao.getHistory(id)

    override suspend fun insert(history: WeightsExerciseHistory) = weightsExerciseHistoryDao.insert(history)

    override suspend fun update(history: WeightsExerciseHistory) = weightsExerciseHistoryDao.update(history)

    override suspend fun delete(history: WeightsExerciseHistory) = weightsExerciseHistoryDao.delete(history)

    override suspend fun deleteAllForExercise(exercise: Exercise) = weightsExerciseHistoryDao.deleteAllForExercise(exercise.exerciseId)
}
