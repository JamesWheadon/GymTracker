package com.askein.gymtracker.ui.navigation

enum class NavigationRoutes(
    val baseRoute: String,
    val navigationArgument: String = "",
) {
    HOME_SCREEN("home"),
    WORKOUT_SELECTION_SCREEN("liveRecordWorkout"),
    EXERCISE_DETAILS_SCREEN("exercises", navigationArgument = "exerciseId"),
    WORKOUT_DETAILS_SCREEN("workouts", navigationArgument = "workoutId"),
    LIVE_RECORD_WORKOUT_SCREEN(
        WORKOUT_SELECTION_SCREEN.baseRoute,
        navigationArgument = "workoutId"
    ),
    USER_PREFERENCES_SCREEN("userPreferences")
}
