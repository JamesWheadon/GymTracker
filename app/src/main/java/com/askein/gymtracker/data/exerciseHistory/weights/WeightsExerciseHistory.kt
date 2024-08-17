package com.askein.gymtracker.data.exerciseHistory.weights

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "weights_exercise_history")
data class WeightsExerciseHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseId: Int,
    val weight: List<Double>,
    val sets: Int,
    val reps: List<Int>? = null,
    val date: LocalDate,
    val rest: Int? = null,
    val seconds: List<Int>? = null,
    var workoutHistoryId: Int? = null
)
