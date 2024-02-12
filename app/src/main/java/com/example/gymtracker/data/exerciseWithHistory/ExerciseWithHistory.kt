package com.example.gymtracker.data.exerciseWithHistory

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory

data class ExerciseWithHistory(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId",
        entity = WeightsExerciseHistory::class
    )
    val weightsHistory: List<WeightsExerciseHistory>,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId",
        entity = CardioExerciseHistory::class
    )
    val cardioHistory: List<CardioExerciseHistory>
)
