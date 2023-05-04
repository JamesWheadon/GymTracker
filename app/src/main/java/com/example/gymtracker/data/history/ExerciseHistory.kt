package com.example.gymtracker.data.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "history")
data class ExerciseHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var exerciseId: Int,
    var weight: Int,
    var sets: Int,
    var reps: Int,
    var rest: Int,
    val date: Date
)
