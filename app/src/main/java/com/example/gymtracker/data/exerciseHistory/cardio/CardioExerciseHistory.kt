package com.example.gymtracker.data.exerciseHistory.cardio

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "cardio_exercise_history")
data class CardioExerciseHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseId: Int,
    val date: LocalDate,
    val minutes: Int? = null,
    val seconds: Int? = null,
    val calories: Int? = null,
    val distance: Double? = null,
    var workoutHistoryId: Int? = null
)
