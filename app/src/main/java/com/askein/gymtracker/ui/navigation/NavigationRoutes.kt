package com.askein.gymtracker.ui.navigation

enum class NavigationRoutes(
    val baseRoute: String,
    val idArgument: String = "",
    val dateArgument: String = ""
) {
    HOME_SCREEN("home"),
    WORKOUT_SELECTION_SCREEN("liveRecordWorkout"),
    EXERCISE_DETAILS_SCREEN("exercises", idArgument = "exerciseId", dateArgument = "chosenDate"),
    WORKOUT_DETAILS_SCREEN("workouts", idArgument = "workoutId", dateArgument = "chosenDate"),
    LIVE_RECORD_WORKOUT_SCREEN(WORKOUT_SELECTION_SCREEN.baseRoute, idArgument = "workoutId"),
    USER_PREFERENCES_SCREEN("userPreferences")
}
