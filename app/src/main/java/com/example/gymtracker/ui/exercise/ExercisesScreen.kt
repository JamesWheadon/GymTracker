package com.example.gymtracker.ui.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.create.CreateExerciseScreen
import com.example.gymtracker.ui.navigation.HomeNavigationInformation
import com.example.gymtracker.ui.navigation.HomeScreenCardWrapper
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.navigation.NavigationRoutes
import com.example.gymtracker.ui.theme.GymTrackerTheme

object ExercisesRoute : NavigationRoute {
    override val route = NavigationRoutes.EXERCISES_SCREEN.baseRoute
}

@Composable
fun ExercisesScreen(
    navController: NavHostController,
    exerciseNavigationFunction: (Int) -> Unit,
    homeNavigationOptions: Map<HomeNavigationInformation, Boolean>,
    modifier: Modifier = Modifier,
    viewModel: ExercisesScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val exerciseListUiState by viewModel.exerciseListUiState.collectAsState()
    ExercisesScreen(
        exerciseListUiState = exerciseListUiState,
        navController = navController,
        exerciseNavigationFunction = exerciseNavigationFunction,
        homeNavigationOptions = homeNavigationOptions,
        modifier = modifier
    )
}

@Composable
fun ExercisesScreen(
    exerciseListUiState: ExerciseListUiState,
    navController: NavHostController,
    exerciseNavigationFunction: (Int) -> Unit,
    homeNavigationOptions: Map<HomeNavigationInformation, Boolean>,
    modifier: Modifier = Modifier
) {
    var showCreate by remember { mutableStateOf(false) }
    HomeScreenCardWrapper(
        title = "My Exercises",
        navController = navController,
        homeNavigationOptions = homeNavigationOptions,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreate = true },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.Black,
                    contentDescription = "Add Exercise"
                )
            }
        }
    ){
        ExercisesScreen(
            exerciseListUiState = exerciseListUiState,
            exerciseNavigationFunction = exerciseNavigationFunction,
            modifier = modifier
        )
    }
    if (showCreate) {
        Dialog(
            onDismissRequest = { showCreate = false }
        ) {
            CreateExerciseScreen(
                onDismiss = { showCreate = false }
            )
        }
    }
}

@Composable
private fun ExercisesScreen(
    exerciseListUiState: ExerciseListUiState,
    exerciseNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(exerciseListUiState.exerciseList) { exercise ->
            ExerciseCard(
                exercise = exercise,
                navigationFunction = exerciseNavigationFunction
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExercisesScreen(
            exerciseListUiState = ExerciseListUiState(
                listOf(
                    ExerciseUiState(0, "Curls", "Biceps", "Dumbbells"),
                    ExerciseUiState(1, "Dips", "Triceps", "Dumbbells And Bars"),
                    ExerciseUiState(
                        2,
                        "Testing what happens if someone decides to have a ridiculously long exercise name",
                        "Lats",
                        "Dumbbells"
                    ),
                )
            ),
            exerciseNavigationFunction = { }
        )
    }
}
