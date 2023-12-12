package com.example.gymtracker.ui.navigation

enum class NavigationRoutes(val baseRoute: String, val navigationArgument: String = "", val homeRoute: Boolean = false, val homeNavigationTitle: String = "") {
    EXERCISES_SCREEN("exercises", homeRoute = true, homeNavigationTitle = "Exercises"),
    WORKOUTS_SCREEN("workouts", homeRoute = true, homeNavigationTitle = "Workouts"),
    EXERCISE_DETAILS_SCREEN(EXERCISES_SCREEN.baseRoute, navigationArgument = "exerciseId"),
    WORKOUT_DETAILS_SCREEN(WORKOUTS_SCREEN.baseRoute, navigationArgument = "workoutId")
}
