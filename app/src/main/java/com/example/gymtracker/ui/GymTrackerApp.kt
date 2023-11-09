package com.example.gymtracker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymtracker.ui.exercise.ExercisesScreen
import com.example.gymtracker.ui.exercise.ExercisesRoute
import com.example.gymtracker.ui.exercise.details.ExerciseDetailsRoute
import com.example.gymtracker.ui.exercise.details.ExerciseDetailsScreen
import com.example.gymtracker.ui.navigation.getHomeNavigationOptionsForRoute
import com.example.gymtracker.ui.workout.WorkoutsRoute
import com.example.gymtracker.ui.workout.WorkoutsScreen
import com.example.gymtracker.ui.workout.details.WorkoutDetailsRoute
import com.example.gymtracker.ui.workout.details.WorkoutDetailsScreen

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
            arguments = listOf(navArgument(ExerciseDetailsRoute.navArgument) { type = NavType.IntType })) {
            ExerciseDetailsScreen(
                navController = navController
            )
        }
        composable(WorkoutDetailsRoute.route,
            arguments = listOf(navArgument(WorkoutDetailsRoute.navArgument) { type = NavType.IntType })) {
            WorkoutDetailsScreen(
                navController = navController,
                exerciseNavigationFunction = { id: Int -> navController.navigate(ExerciseDetailsRoute.getRouteForNavArgument(id)) },
            )
        }
    }
}
