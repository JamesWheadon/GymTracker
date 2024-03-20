package com.askein.gymtracker.data.workoutWithExercises

import androidx.room.Embedded
import androidx.room.Relation
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.askein.gymtracker.data.workoutHistory.WorkoutHistory

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
