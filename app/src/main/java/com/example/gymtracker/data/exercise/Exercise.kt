package com.example.gymtracker.data.exercise

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int = 0,
    var name: String,
    var muscleGroup: String,
    var equipment: String
)
