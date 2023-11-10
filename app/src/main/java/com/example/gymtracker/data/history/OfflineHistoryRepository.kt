package com.example.gymtracker.data.history

import com.example.gymtracker.data.exercise.Exercise
import kotlinx.coroutines.flow.Flow

class OfflineHistoryRepository(private val historyDao: HistoryDao) : HistoryRepository {
    override fun getHistoryStream(id: Int): Flow<ExerciseHistory?> = historyDao.getHistory(id)

    override fun getFullExerciseHistoryStream(exerciseId: Int): Flow<List<ExerciseHistory>?> =
        historyDao.getFullExerciseHistory(exerciseId)

    override fun getLatestHistoryStream(id: Int): Flow<ExerciseHistory?> =
        historyDao.getLatestExerciseHistory(id)

    override fun getRecentHistoryStream(id: Int, days: Int): Flow<List<ExerciseHistory>?> =
        historyDao.getRecentExerciseHistory(id, days)

    override suspend fun insertHistory(history: ExerciseHistory) = historyDao.insert(history)

    override suspend fun update(history: ExerciseHistory) = historyDao.update(history)

    override suspend fun delete(history: ExerciseHistory) = historyDao.delete(history)

    override suspend fun deleteAllForExercise(exercise: Exercise) = historyDao.deleteAllForExercise(exercise.exerciseId)
}
