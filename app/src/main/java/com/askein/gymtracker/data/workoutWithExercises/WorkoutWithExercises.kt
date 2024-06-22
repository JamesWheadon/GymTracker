package com.askein.gymtracker.data.workoutWithExercises

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRef
import com.askein.gymtracker.data.workoutHistory.WorkoutHistory

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
        entityColumn = "workoutId",
        entity = WorkoutHistory::class
    )
    val workoutHistory: List<WorkoutHistoryWithExerciseHistory>,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId",
        entity = WorkoutExerciseCrossRef::class
    )
    val exerciseOrder: List<WorkoutExerciseCrossRef>
)
