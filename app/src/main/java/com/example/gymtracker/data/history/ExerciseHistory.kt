package com.example.gymtracker.data.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class ExerciseHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseId: Int,
    val weight: Int,
    val sets: Int,
    val reps: Int,
    val rest: Int
)
