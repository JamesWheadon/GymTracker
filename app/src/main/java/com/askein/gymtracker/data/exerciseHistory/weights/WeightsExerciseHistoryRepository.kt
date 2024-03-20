package com.askein.gymtracker.data.exerciseHistory.weights

import com.askein.gymtracker.data.exercise.Exercise
import kotlinx.coroutines.flow.Flow

interface WeightsExerciseHistoryRepository {
    fun getHistoryStream(id: Int): Flow<WeightsExerciseHistory?>

    suspend fun insert(history: WeightsExerciseHistory)

    suspend fun update(history: WeightsExerciseHistory)

    suspend fun delete(history: WeightsExerciseHistory)

    suspend fun deleteAllForExercise(exercise: Exercise)
}
