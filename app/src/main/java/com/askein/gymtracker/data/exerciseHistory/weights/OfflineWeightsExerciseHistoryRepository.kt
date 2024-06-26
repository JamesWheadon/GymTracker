package com.askein.gymtracker.data.exerciseHistory.weights

import com.askein.gymtracker.data.exercise.Exercise

class OfflineWeightsExerciseHistoryRepository(private val weightsExerciseHistoryDao: WeightsExerciseHistoryDao) :
    WeightsExerciseHistoryRepository {

    override suspend fun insert(history: WeightsExerciseHistory) = weightsExerciseHistoryDao.insert(history)

    override suspend fun update(history: WeightsExerciseHistory) = weightsExerciseHistoryDao.update(history)

    override suspend fun delete(history: WeightsExerciseHistory) = weightsExerciseHistoryDao.delete(history)

    override suspend fun deleteAllForExercise(exercise: Exercise) = weightsExerciseHistoryDao.deleteAllForExercise(exercise.exerciseId)

    override suspend fun deleteAllForWorkoutHistory(workoutHistoryId: Int) = weightsExerciseHistoryDao.deleteAllForWorkoutHistory(workoutHistoryId)
}
