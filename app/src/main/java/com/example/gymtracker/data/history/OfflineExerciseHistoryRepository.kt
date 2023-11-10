package com.example.gymtracker.data.history

import com.example.gymtracker.data.exercise.Exercise
import kotlinx.coroutines.flow.Flow

class OfflineExerciseHistoryRepository(private val exerciseHistoryDao: ExerciseHistoryDao) : ExerciseHistoryRepository {
    override fun getHistoryStream(id: Int): Flow<ExerciseHistory?> = exerciseHistoryDao.getHistory(id)

    override fun getFullExerciseHistoryStream(exerciseId: Int): Flow<List<ExerciseHistory>?> =
        exerciseHistoryDao.getFullExerciseHistory(exerciseId)

    override fun getLatestHistoryStream(id: Int): Flow<ExerciseHistory?> =
        exerciseHistoryDao.getLatestExerciseHistory(id)

    override fun getRecentHistoryStream(id: Int, days: Int): Flow<List<ExerciseHistory>?> =
        exerciseHistoryDao.getRecentExerciseHistory(id, days)

    override suspend fun insertHistory(history: ExerciseHistory) = exerciseHistoryDao.insert(history)

    override suspend fun update(history: ExerciseHistory) = exerciseHistoryDao.update(history)

    override suspend fun delete(history: ExerciseHistory) = exerciseHistoryDao.delete(history)

    override suspend fun deleteAllForExercise(exercise: Exercise) = exerciseHistoryDao.deleteAllForExercise(exercise.exerciseId)
}
