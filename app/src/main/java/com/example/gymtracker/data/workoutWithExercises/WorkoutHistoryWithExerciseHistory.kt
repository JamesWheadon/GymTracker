package com.example.gymtracker.data.workoutWithExercises

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistory

data class WorkoutHistoryWithExerciseHistory(
    @Embedded val workoutHistory: WorkoutHistory,
    @Relation(
        parentColumn = "workoutHistoryId",
        entityColumn = "workoutHistoryId"
    )
    val weightsExercises: List<WeightsExerciseHistory>,
    @Relation(
        parentColumn = "workoutHistoryId",
        entityColumn = "workoutHistoryId"
    )
    val cardioExercises: List<CardioExerciseHistory>
)
