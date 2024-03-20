package com.askein.gymtracker.data.exerciseWithHistory

import kotlinx.coroutines.flow.Flow

interface ExerciseWithHistoryRepository {
    fun getExerciseWithHistoryStream(exerciseId: Int): Flow<ExerciseWithHistory>
}
