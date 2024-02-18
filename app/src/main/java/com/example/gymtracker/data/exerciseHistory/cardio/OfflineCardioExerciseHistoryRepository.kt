package com.example.gymtracker.data.exerciseHistory.cardio

import com.example.gymtracker.data.exercise.Exercise
import kotlinx.coroutines.flow.Flow

class OfflineCardioExerciseHistoryRepository(private val cardioExerciseHistoryDao: CardioExerciseHistoryDao) :
    CardioExerciseHistoryRepository {
    override fun getStream(id: Int): Flow<CardioExerciseHistory?> = cardioExerciseHistoryDao.get(id)

    override fun getFullExerciseHistoryStream(exerciseId: Int): Flow<List<CardioExerciseHistory>?> =
        cardioExerciseHistoryDao.getFullExerciseHistory(exerciseId)

    override suspend fun insert(history: CardioExerciseHistory) = cardioExerciseHistoryDao.insert(history)

    override suspend fun update(history: CardioExerciseHistory) = cardioExerciseHistoryDao.update(history)

    override suspend fun delete(history: CardioExerciseHistory) = cardioExerciseHistoryDao.delete(history)

    override suspend fun deleteAllForExercise(exercise: Exercise) = cardioExerciseHistoryDao.deleteAllForExercise(exercise.exerciseId)
}
