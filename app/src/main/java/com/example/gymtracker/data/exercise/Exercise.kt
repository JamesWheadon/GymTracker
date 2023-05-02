package com.example.gymtracker.data.exercise

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    val muscleGroup: String,
    val equipment: String
)
