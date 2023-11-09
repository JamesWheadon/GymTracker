package com.example.gymtracker.data.workoutExerciseCrossRef

import androidx.room.Entity

@Entity(tableName = "workoutsExercises", primaryKeys = ["workoutId", "exerciseId"])
data class WorkoutExerciseCrossRef(
    val workoutId: Int,
    val exerciseId: Int
)
