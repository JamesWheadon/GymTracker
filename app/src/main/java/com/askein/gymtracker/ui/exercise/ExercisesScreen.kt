package com.askein.gymtracker.ui.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.exercise.create.CreateExerciseScreen
import com.askein.gymtracker.ui.navigation.HomeNavigationInformation
import com.askein.gymtracker.ui.navigation.HomeScreenWrapper
import com.askein.gymtracker.ui.navigation.NavigationRoute
import com.askein.gymtracker.ui.navigation.NavigationRoutes
import com.askein.gymtracker.ui.theme.GymTrackerTheme

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
    HomeScreenWrapper(
        title = stringResource(id = R.string.my_exercises),
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
                    contentDescription = stringResource(id = R.string.add_exercise)
                )
            }
        }
    ) {
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
fun ExercisesScreen(
    exerciseListUiState: ExerciseListUiState,
    exerciseNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(exerciseListUiState.exerciseList.sortedBy { it.name }) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    navigationFunction = exerciseNavigationFunction
                )
            }
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

@Preview(showBackground = true)
@Composable
fun ExerciseScreenCardPreview() {
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
            exerciseNavigationFunction = { },
            navController = rememberNavController(),
            homeNavigationOptions = mapOf()
        )
    }
}
