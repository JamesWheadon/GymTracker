package com.example.gymtracker.data.workoutHistory

interface WorkoutHistoryRepository {
    suspend fun insert(workoutHistoryWithExerciseHistory: WorkoutHistoryWithExerciseHistory)}
