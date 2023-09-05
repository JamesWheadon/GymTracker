package com.example.gymtracker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymtracker.ui.exercise.create.CreateExerciseScreen
import com.example.gymtracker.ui.exercise.ExerciseScreen
import com.example.gymtracker.ui.exercise.details.ExerciseDetailsScreen
import com.example.gymtracker.ui.history.RecordHistoryScreen

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
                newExerciseFunction = { navController.navigate("newExercise") },
                exerciseNavigationFunction = { id: Int -> navController.navigate("exercises/$id") }
            )
        }
        composable(route = "newExercise") {
            CreateExerciseScreen(
                navigateFunction = { navController.navigate("exercises") }
            )
        }
        composable("exercises/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })) {
            ExerciseDetailsScreen(
                recordExerciseNavigationFunction = { id: Int -> navController.navigate("exercises/$id/new") }
            )
        }
        composable("exercises/{exerciseId}/new",
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })) {
            RecordHistoryScreen (
                returnNavigationFunction = { id: Int -> navController.navigate("exercises/$id") }
            )
        }
    }
}