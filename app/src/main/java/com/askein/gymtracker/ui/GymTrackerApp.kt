package com.askein.gymtracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.askein.gymtracker.ui.exercise.details.ExerciseDetailsRoute
import com.askein.gymtracker.ui.exercise.details.ExerciseDetailsScreen
import com.askein.gymtracker.ui.home.HomeScreen
import com.askein.gymtracker.ui.home.HomeScreenRoute
import com.askein.gymtracker.ui.navigation.NavigationRoutes.EXERCISE_DETAILS_SCREEN
import com.askein.gymtracker.ui.navigation.NavigationRoutes.LIVE_RECORD_WORKOUT_SCREEN
import com.askein.gymtracker.ui.navigation.NavigationRoutes.WORKOUT_DETAILS_SCREEN
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesRoute
import com.askein.gymtracker.ui.user.UserPreferencesScreen
import com.askein.gymtracker.ui.user.UserPreferencesViewModel
import com.askein.gymtracker.ui.workout.LiveRecordChooseWorkoutsScreen
import com.askein.gymtracker.ui.workout.WorkoutSelectionScreenRoute
import com.askein.gymtracker.ui.workout.details.WorkoutDetailsRoute
import com.askein.gymtracker.ui.workout.details.WorkoutDetailsScreen
import com.askein.gymtracker.ui.workout.history.create.live.LiveRecordWorkout
import com.askein.gymtracker.ui.workout.history.create.live.LiveRecordWorkoutRoute
import java.time.LocalDate

@Composable
fun GymTrackerApp(
    navController: NavHostController = rememberNavController(),
    userPreferencesViewModel: UserPreferencesViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val userPreferencesUiState = userPreferencesViewModel.uiState.collectAsState().value
    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
        NavHost(
            navController = navController,
            startDestination = HomeScreenRoute.route,
        ) {
            composable(
                route = HomeScreenRoute.route
            ) {
                HomeScreen(
                    navController = navController,
                    exerciseNavigationFunction = { id: Int, date: LocalDate? ->
                        navController.navigate(
                            ExerciseDetailsRoute.getRouteForNavArguments(id, date)
                        )
                    },
                    workoutNavigationFunction = { id: Int, date: LocalDate? ->
                        navController.navigate(
                            WorkoutDetailsRoute.getRouteForNavArguments(id, date)
                        )
                    }
                )
            }
            composable(
                route = ExerciseDetailsRoute.route,
                arguments = listOf(
                    navArgument(EXERCISE_DETAILS_SCREEN.idArgument) {
                        type = NavType.IntType
                    },
                    navArgument(EXERCISE_DETAILS_SCREEN.dateArgument) {
                        type = NavType.StringType
                    }
                )
            ) {
                ExerciseDetailsScreen(
                    navController = navController
                )
            }
            composable(
                route = WorkoutDetailsRoute.route,
                arguments = listOf(
                    navArgument(WORKOUT_DETAILS_SCREEN.idArgument) {
                        type = NavType.IntType
                    },
                    navArgument(WORKOUT_DETAILS_SCREEN.dateArgument) {
                        type = NavType.StringType
                    }
                )
            ) {
                WorkoutDetailsScreen(
                    navController = navController,
                    exerciseNavigationFunction = { id: Int, date: LocalDate? ->
                        navController.navigate(
                            ExerciseDetailsRoute.getRouteForNavArguments(id, date)
                        )
                    },
                )
            }
            composable(route = WorkoutSelectionScreenRoute.route) {
                LiveRecordChooseWorkoutsScreen(
                    navController = navController,
                    workoutNavigationFunction = { id: Int, _: LocalDate? ->
                        navController.navigate(
                            LiveRecordWorkoutRoute.getRouteForNavArgument(id)
                        )
                    }
                )
            }
            composable(
                route = LiveRecordWorkoutRoute.route,
                arguments = listOf(navArgument(LIVE_RECORD_WORKOUT_SCREEN.idArgument) {
                    type = NavType.IntType
                })
            ) {
                LiveRecordWorkout(
                    navController = navController
                )
            }
            composable(route = UserPreferencesRoute.route) {
                UserPreferencesScreen(
                    navController = navController
                )
            }
        }
    }
}
