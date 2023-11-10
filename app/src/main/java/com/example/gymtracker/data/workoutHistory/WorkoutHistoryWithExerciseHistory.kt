package com.example.gymtracker.data.workoutHistory

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymtracker.data.history.ExerciseHistory

data class WorkoutHistoryWithExerciseHistory(
    @Embedded val workoutHistory: WorkoutHistory,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutHistoryId"
    )
    val exercises: List<ExerciseHistory>
)
