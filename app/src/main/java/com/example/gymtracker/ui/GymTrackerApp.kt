package com.example.gymtracker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymtracker.ui.exercise.ExerciseScreen
import com.example.gymtracker.ui.exercise.details.ExerciseDetailsScreen

@Composable
fun GymTrackerApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "exercises",
    ) {
        composable(route = "exercises") {
            ExerciseScreen(
                exerciseNavigationFunction = { id: Int -> navController.navigate("exercises/$id") }
            )
        }
        composable("exercises/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })) {
            ExerciseDetailsScreen(
                backNavigationFunction = { navController.navigate("exercises") }
            )
        }
    }
}