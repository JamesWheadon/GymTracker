package com.example.gymtracker.ui.navigation

enum class NavigationArguments(val route: String, val homeRoute: Boolean = false, val homeNavigationTitle: String = "") {
    EXERCISES_SCREEN("exercises", true, "Exercises"),
    WORKOUTS_SCREEN("workouts", true, "Workouts"),
    EXERCISE_DETAILS_NAV_ARGUMENT("exerciseId"),
    WORKOUTS_DETAILS_NAV_ARGUMENT("workoutId")
}
