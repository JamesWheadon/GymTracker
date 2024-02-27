package com.example.gymtracker.fake

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exerciseWithHistory.ExerciseWithHistory
import com.example.gymtracker.data.exerciseWithHistory.ExerciseWithHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeExerciseWithHistoryRepository: ExerciseWithHistoryRepository {

    val exerciseWithHistory = ExerciseWithHistory(
        exercise = Exercise(
            exerciseId = 1,
            name = "Test",
            equipment = "",
            muscleGroup = ""
        ),
        weightsHistory = listOf(),
        cardioHistory = listOf()
    )

    override fun getExerciseWithHistoryStream(exerciseId: Int): Flow<ExerciseWithHistory> = flowOf(exerciseWithHistory)
}
