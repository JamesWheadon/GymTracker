package com.example.gymtracker.ui.exercise.details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.ui.ActionConfirmation
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.create.ExerciseInformationForm
import com.example.gymtracker.ui.exercise.history.RecordExerciseHistoryScreen
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.navigation.NavigationRoutes.EXERCISE_DETAILS_SCREEN
import com.example.gymtracker.ui.navigation.TopBar
import com.example.gymtracker.ui.theme.GymTrackerTheme
import java.time.LocalDate

object ExerciseDetailsRoute : NavigationRoute {
    override val route =
        "${EXERCISE_DETAILS_SCREEN.baseRoute}/{${EXERCISE_DETAILS_SCREEN.navigationArgument}}"

    fun getRouteForNavArgument(navArgument: Int): String =
        "${EXERCISE_DETAILS_SCREEN.baseRoute}/${navArgument}"
}

@Composable
fun ExerciseDetailsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ExerciseDetailsViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    ExerciseDetailsScreen(
        uiState = uiState,
        navController = navController,
        updateFunction = { exercise -> viewModel.updateExercise(exercise) },
        deleteFunction = { exercise -> viewModel.deleteExercise(exercise) },
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailsScreen(
    uiState: ExerciseDetailsUiState,
    navController: NavHostController,
    updateFunction: (Exercise) -> Unit,
    deleteFunction: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    var showRecord by remember { mutableStateOf(false) }
    var updateExercise by remember { mutableStateOf(false) }
    var deleteExercise by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                text = uiState.exercise.name,
                navController = navController,
                editFunction = { updateExercise = true },
                deleteFunction = { deleteExercise = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showRecord = true },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.Black,
                    contentDescription = "Add Workout"
                )
            }
        }
    ) { innerPadding ->
        if (uiState.exercise.equipment != "") {
            WeightsExerciseDetailsScreen(innerPadding, uiState)
        } else {
            CardioExerciseDetailsScreen(innerPadding, uiState)
        }
    }
    if (showRecord) {
        Dialog(
            onDismissRequest = { showRecord = false }
        ) {
            RecordExerciseHistoryScreen(
                exercise = uiState.toExerciseUiState(),
                onDismiss = { showRecord = false }
            )
        }
    }
    if (updateExercise) {
        Dialog(
            onDismissRequest = { updateExercise = false }
        ) {
            ExerciseInformationForm(
                formTitle = "Update Exercise",
                buttonText = "Save",
                onDismiss = { updateExercise = false },
                createFunction = updateFunction,
                exercise = uiState.toExerciseUiState()
            )
        }
    }
    if (deleteExercise) {
        Dialog(
            onDismissRequest = { deleteExercise = false }
        ) {
            ActionConfirmation(
                actionTitle = "Delete ${uiState.exercise.name} Exercise?",
                confirmFunction = {
                    deleteFunction(uiState.toExercise())
                    navController.popBackStack()
                },
                cancelFunction = { deleteExercise = false })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseDetailsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        WeightsExerciseDetailsScreen(
            innerPadding = PaddingValues(),
            uiState = ExerciseDetailsUiState(
                exercise = ExerciseUiState(
                    name = "Curls",
                    muscleGroup = "Biceps",
                    equipment = "Dumbbells"
                ),
                weightsHistory = listOf(
                    WeightsExerciseHistoryUiState(
                        id = 1,
                        weight = 13.0,
                        sets = 1,
                        reps = 2,
                        rest = 1,
                        date = LocalDate.now().minusDays(5)
                    )
                )
            )
        )
    }
}
