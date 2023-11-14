package com.example.gymtracker.data.exerciseHistory

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "exercise_history")
data class ExerciseHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var exerciseId: Int,
    var weight: Double,
    var sets: Int,
    var reps: Int,
    val date: LocalDate,
    var workoutHistoryId: Int? = null
)
