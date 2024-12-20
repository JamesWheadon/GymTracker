package com.askein.gymtracker.ui.exercise.details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.askein.gymtracker.R
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.ui.ActionConfirmation
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.create.ExerciseInformationForm
import com.askein.gymtracker.ui.exercise.history.RecordExerciseHistoryScreen
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.navigation.NavigationRoute
import com.askein.gymtracker.ui.navigation.NavigationRoutes.EXERCISE_DETAILS_SCREEN
import com.askein.gymtracker.ui.navigation.TopBar
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.util.convertLocalDateToString
import com.askein.gymtracker.util.convertStringToLocalDate
import java.time.LocalDate

object ExerciseDetailsRoute : NavigationRoute {
    override val route =
        "${EXERCISE_DETAILS_SCREEN.baseRoute}/{${EXERCISE_DETAILS_SCREEN.idArgument}}/{${EXERCISE_DETAILS_SCREEN.dateArgument}}"

    fun getRouteForNavArguments(navArgument: Int, chosenDate: LocalDate?): String =
        "${EXERCISE_DETAILS_SCREEN.baseRoute}/${navArgument}/${convertLocalDateToString(chosenDate)}"
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
    val chosenDate = convertStringToLocalDate(viewModel.chosenDate)
    ExerciseDetailsScreen(
        uiState = uiState,
        navController = navController,
        updateFunction = { exercise -> viewModel.updateExercise(exercise) },
        deleteFunction = { exercise -> viewModel.deleteExercise(exercise) },
        chosenDate = chosenDate,
        modifier = modifier
    )
}

@Composable
fun ExerciseDetailsScreen(
    uiState: ExerciseDetailsUiState,
    navController: NavHostController,
    updateFunction: (ExerciseUiState) -> Unit,
    deleteFunction: (ExerciseUiState) -> Unit,
    chosenDate: LocalDate?,
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
                    contentDescription = stringResource(id = R.string.record_exercise)
                )
            }
        }
    ) { innerPadding ->
        when (uiState.exercise.type) {
            ExerciseType.WEIGHTS -> {
                WeightsExerciseDetailsScreen(
                    innerPadding = innerPadding,
                    uiState = uiState,
                    chosenDate = chosenDate
                )
            }
            ExerciseType.CARDIO -> {
                CardioExerciseDetailsScreen(
                    innerPadding = innerPadding,
                    uiState = uiState,
                    chosenDate = chosenDate
                )
            }
            ExerciseType.CALISTHENICS -> {
                CalisthenicsExerciseDetailsScreen(
                    innerPadding = innerPadding,
                    uiState = uiState,
                    chosenDate = chosenDate
                )
            }
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
                formTitle = R.string.update_exercise,
                buttonText = R.string.save,
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
                actionTitle = stringResource(id = R.string.delete_exercise, uiState.exercise.name),
                confirmFunction = {
                    deleteFunction(uiState.toExerciseUiState())
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
                        weight = listOf(13.0),
                        sets = 1,
                        reps = listOf(2),
                        rest = 1,
                        date = LocalDate.now().minusDays(5)
                    )
                )
            ),
            chosenDate = null
        )
    }
}
