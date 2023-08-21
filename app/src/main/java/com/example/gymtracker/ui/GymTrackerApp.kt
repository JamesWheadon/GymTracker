package com.example.gymtracker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymtracker.ui.exercise.CreateExerciseScreen
import com.example.gymtracker.ui.exercise.ExerciseScreen

@Composable
fun GymTrackerApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "Exercises",
    ) {
        composable(route = "Exercises") {
            ExerciseScreen(
                newExerciseFunction = { navController.navigate("NewExercise") }
            )
        }
        composable(route = "NewExercise") {
            CreateExerciseScreen(
                navigateFunction = { navController.navigate("Exercises") }
            )
        }
    }
}