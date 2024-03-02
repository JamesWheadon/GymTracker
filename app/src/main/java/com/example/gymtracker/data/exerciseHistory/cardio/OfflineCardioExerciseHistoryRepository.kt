package com.example.gymtracker.data.exerciseHistory.cardio

import com.example.gymtracker.data.exercise.Exercise

class OfflineCardioExerciseHistoryRepository(private val cardioExerciseHistoryDao: CardioExerciseHistoryDao) :
    CardioExerciseHistoryRepository {

    override suspend fun insert(history: CardioExerciseHistory) = cardioExerciseHistoryDao.insert(history)

    override suspend fun update(history: CardioExerciseHistory) = cardioExerciseHistoryDao.update(history)

    override suspend fun delete(history: CardioExerciseHistory) = cardioExerciseHistoryDao.delete(history)

    override suspend fun deleteAllForExercise(exercise: Exercise) = cardioExerciseHistoryDao.deleteAllForExercise(exercise.exerciseId)
}
