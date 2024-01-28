package com.example.gymtracker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymtracker.ui.exercise.ExercisesRoute
import com.example.gymtracker.ui.exercise.ExercisesScreen
import com.example.gymtracker.ui.exercise.details.ExerciseDetailsRoute
import com.example.gymtracker.ui.exercise.details.ExerciseDetailsScreen
import com.example.gymtracker.ui.navigation.NavigationRoutes.EXERCISE_DETAILS_SCREEN
import com.example.gymtracker.ui.navigation.NavigationRoutes.LIVE_RECORD_WORKOUT_SCREEN
import com.example.gymtracker.ui.navigation.NavigationRoutes.WORKOUT_DETAILS_SCREEN
import com.example.gymtracker.ui.navigation.getHomeNavigationOptionsForRoute
import com.example.gymtracker.ui.workout.LiveRecordChooseWorkoutsScreen
import com.example.gymtracker.ui.workout.WorkoutSelectionScreenRoute
import com.example.gymtracker.ui.workout.WorkoutsRoute
import com.example.gymtracker.ui.workout.WorkoutsScreen
import com.example.gymtracker.ui.workout.details.WorkoutDetailsRoute
import com.example.gymtracker.ui.workout.details.WorkoutDetailsScreen
import com.example.gymtracker.ui.workout.history.create.live.LiveRecordWorkout
import com.example.gymtracker.ui.workout.history.create.live.LiveRecordWorkoutRoute

@Composable
fun GymTrackerApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ExercisesRoute.route,
    ) {
        composable(route = ExercisesRoute.route) {
            ExercisesScreen(
                navController = navController,
                exerciseNavigationFunction = { id: Int -> navController.navigate(ExerciseDetailsRoute.getRouteForNavArgument(id)) },
                homeNavigationOptions = getHomeNavigationOptionsForRoute(ExercisesRoute, navController)
            )
        }
        composable(route = WorkoutsRoute.route) {
            WorkoutsScreen(
                navController = navController,
                workoutNavigationFunction = { id: Int -> navController.navigate(WorkoutDetailsRoute.getRouteForNavArgument(id)) },
                homeNavigationOptions = getHomeNavigationOptionsForRoute(WorkoutsRoute, navController)
            )
        }
        composable(ExerciseDetailsRoute.route,
            arguments = listOf(navArgument(EXERCISE_DETAILS_SCREEN.navigationArgument) { type = NavType.IntType })
        ) {
            ExerciseDetailsScreen(
                navController = navController
            )
        }
        composable(WorkoutDetailsRoute.route,
            arguments = listOf(navArgument(WORKOUT_DETAILS_SCREEN.navigationArgument) { type = NavType.IntType })
        ) {
            WorkoutDetailsScreen(
                navController = navController,
                exerciseNavigationFunction = { id: Int -> navController.navigate(ExerciseDetailsRoute.getRouteForNavArgument(id)) },
            )
        }
        composable(WorkoutSelectionScreenRoute.route) {
            LiveRecordChooseWorkoutsScreen(
                navController = navController,
                workoutNavigationFunction = { id: Int -> navController.navigate(LiveRecordWorkoutRoute.getRouteForNavArgument(id)) }
            )
        }
        composable(LiveRecordWorkoutRoute.route,
            arguments = listOf(navArgument(LIVE_RECORD_WORKOUT_SCREEN.navigationArgument) { type = NavType.IntType })
        ) {
            LiveRecordWorkout(
                navController = navController
            )
        }
    }
}
