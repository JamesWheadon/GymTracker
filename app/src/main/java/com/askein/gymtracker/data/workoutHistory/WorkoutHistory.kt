package com.askein.gymtracker.data.workoutHistory

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workout_history")
data class WorkoutHistory(
    @PrimaryKey(autoGenerate = true)
    val workoutHistoryId: Int = 0,
    val workoutId: Int,
    val date: LocalDate
)
