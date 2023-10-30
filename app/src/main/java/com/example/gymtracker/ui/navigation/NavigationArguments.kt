package com.example.gymtracker.ui.navigation

enum class NavigationArguments(val routeName: String) {
    EXERCISES_SCREEN("exercises"),
    WORKOUTS_SCREEN("workouts"),
    EXERCISE_DETAILS_NAV_ARGUMENT("exerciseId"),
    WORKOUTS_DETAILS_NAV_ARGUMENT("workoutId")
}
