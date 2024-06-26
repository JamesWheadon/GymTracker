package com.askein.gymtracker.data.exerciseHistory.weights

import com.askein.gymtracker.data.exercise.Exercise

interface WeightsExerciseHistoryRepository {

    suspend fun insert(history: WeightsExerciseHistory)

    suspend fun update(history: WeightsExerciseHistory)

    suspend fun delete(history: WeightsExerciseHistory)

    suspend fun deleteAllForExercise(exercise: Exercise)

    suspend fun deleteAllForWorkoutHistory(workoutHistoryId: Int)
}
