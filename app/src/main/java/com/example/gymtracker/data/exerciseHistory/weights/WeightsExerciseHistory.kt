package com.example.gymtracker.data.exerciseHistory.weights

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "weights_exercise_history")
data class WeightsExerciseHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseId: Int,
    val weight: Double,
    val sets: Int,
    val reps: Int,
    val date: LocalDate,
    val rest: Int? = null,
    var workoutHistoryId: Int? = null
)
