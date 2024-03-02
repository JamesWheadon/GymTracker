package com.example.gymtracker.ui.navigation

enum class NavigationRoutes(
    val baseRoute: String,
    val navigationArgument: String = "",
    val homeRoute: Boolean = false,
    val homeNavigationTitle: String = ""
) {
    EXERCISES_SCREEN("exercises", homeRoute = true, homeNavigationTitle = "Exercises"),
    WORKOUTS_SCREEN("workouts", homeRoute = true, homeNavigationTitle = "Workouts"),
    WORKOUT_SELECTION_SCREEN("liveRecordWorkout"),
    EXERCISE_DETAILS_SCREEN(EXERCISES_SCREEN.baseRoute, navigationArgument = "exerciseId"),
    WORKOUT_DETAILS_SCREEN(WORKOUTS_SCREEN.baseRoute, navigationArgument = "workoutId"),
    LIVE_RECORD_WORKOUT_SCREEN(
        WORKOUT_SELECTION_SCREEN.baseRoute,
        navigationArgument = "workoutId"
    ),
    USER_PREFERENCES_SCREEN("userPreferences")
}
