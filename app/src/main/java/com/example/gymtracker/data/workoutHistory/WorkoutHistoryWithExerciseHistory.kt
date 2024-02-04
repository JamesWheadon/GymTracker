package com.example.gymtracker.data.workoutHistory

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory

data class WorkoutHistoryWithExerciseHistory(
    @Embedded val workoutHistory: WorkoutHistory,
    @Relation(
        parentColumn = "workoutHistoryId",
        entityColumn = "workoutHistoryId"
    )
    val exercises: List<WeightsExerciseHistory>
)
