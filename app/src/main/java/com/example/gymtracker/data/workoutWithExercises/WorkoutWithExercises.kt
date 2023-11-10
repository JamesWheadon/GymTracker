package com.example.gymtracker.data.workoutWithExercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRef
import com.example.gymtracker.data.workoutHistory.WorkoutHistory

data class WorkoutWithExercises(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "exerciseId",
        associateBy = Junction(WorkoutExerciseCrossRef::class)
    )
    val exercises: List<Exercise>,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val workoutHistory: List<WorkoutHistory>
)
