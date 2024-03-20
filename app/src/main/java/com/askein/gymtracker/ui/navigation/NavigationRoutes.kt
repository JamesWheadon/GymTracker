package com.askein.gymtracker.ui.navigation

import androidx.annotation.StringRes
import com.askein.gymtracker.R

enum class NavigationRoutes(
    val baseRoute: String,
    val navigationArgument: String = "",
    val homeRoute: Boolean = false,
    @StringRes val homeNavigationTitle: Int = 0
) {
    EXERCISES_SCREEN("exercises", homeRoute = true, homeNavigationTitle = R.string.exercises),
    WORKOUTS_SCREEN("workouts", homeRoute = true, homeNavigationTitle = R.string.workouts),
    OVERALL_HISTORY_SCREEN("history", homeRoute = true, homeNavigationTitle = R.string.history),
    WORKOUT_SELECTION_SCREEN("liveRecordWorkout"),
    EXERCISE_DETAILS_SCREEN(EXERCISES_SCREEN.baseRoute, navigationArgument = "exerciseId"),
    WORKOUT_DETAILS_SCREEN(WORKOUTS_SCREEN.baseRoute, navigationArgument = "workoutId"),
    LIVE_RECORD_WORKOUT_SCREEN(
        WORKOUT_SELECTION_SCREEN.baseRoute,
        navigationArgument = "workoutId"
    ),
    USER_PREFERENCES_SCREEN("userPreferences")
}
