package com.askein.gymtracker.data.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Int = 0,
    var name: String
)
