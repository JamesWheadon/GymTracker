package com.example.gymtracker.ui.exercise.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.R
import com.example.gymtracker.converters.WeightUnits
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.ActionConfirmation
import com.example.gymtracker.ui.exercise.ExerciseDetailsUiState
import com.example.gymtracker.ui.exercise.ExercisesRoute
import com.example.gymtracker.ui.exercise.create.ExerciseInformationForm
import com.example.gymtracker.ui.exercise.toExercise
import com.example.gymtracker.ui.exercise.toExerciseUiState
import com.example.gymtracker.ui.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.history.RecordHistoryScreen
import com.example.gymtracker.ui.navigation.NavigationArguments
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.navigation.TopBar
import com.example.gymtracker.ui.theme.GymTrackerTheme
import java.time.LocalDate

object ExerciseDetailsRoute : NavigationRoute {
    val navArgument = NavigationArguments.EXERCISE_DETAILS_NAV_ARGUMENT.routeName
    override val route = "${ExercisesRoute.route}/{${navArgument}}"

    fun getRouteForNavArgument(navArgument: Int): String = "${ExercisesRoute.route}/${navArgument}"
}

@Composable
fun ExerciseDetailsScreen(
    backNavigationFunction: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExerciseDetailsViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    uiState.history = viewModel.exerciseHistory.collectAsState().value
    ExerciseDetailsScreen(
        uiState = uiState,
        backNavigationFunction = backNavigationFunction,
        updateFunction = { exercise -> viewModel.updateExercise(exercise) },
        deleteFunction = { exercise -> viewModel.deleteExercise(exercise) },
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailsScreen(
    uiState: ExerciseDetailsUiState,
    backNavigationFunction: () -> Unit,
    updateFunction: (Exercise) -> Unit,
    deleteFunction: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    var showRecord by remember { mutableStateOf(false) }
    var updateExercise by remember { mutableStateOf(false) }
    var deleteExercise by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                text = uiState.name,
                backEnabled = true,
                editEnabled = true,
                deleteEnabled = true,
                navigateBack = backNavigationFunction,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ExerciseInformation(innerPadding, uiState)
            if (uiState.history?.isNotEmpty() == true) {
                ExerciseHistoryDetails(uiState = uiState)
            }
        }
    }
    if (showRecord) {
        Dialog(
            onDismissRequest = { showRecord = false }
        ) {
            RecordHistoryScreen(
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
                actionTitle = "Delete ${uiState.name} Exercise?",
                confirmFunction = {
                    deleteFunction(uiState.toExercise())
                    backNavigationFunction()
                },
                cancelFunction = { deleteExercise = false })
        }
    }
}

@Composable
private fun ExerciseInformation(
    innerPadding: PaddingValues,
    uiState: ExerciseDetailsUiState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        ExerciseDetail(
            exerciseInfo = uiState.muscleGroup,
            iconId = R.drawable.info_48px,
            iconDescription = "exercise icon",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        ExerciseDetail(
            exerciseInfo = uiState.equipment,
            iconId = R.drawable.exercise_filled_48px,
            iconDescription = "equipment icon",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun ExerciseHistoryDetails(uiState: ExerciseDetailsUiState) {
    val timeOptions = listOf("7 Days", "30 Days", "Past Year", "All Time")
    val detailOptions = listOf("Max Weight", "Max Reps", "Max Sets", "Total Weight")
    val currentDate = LocalDate.now()
    val timeOptionToStartTime = mapOf<String, LocalDate>(
        Pair(timeOptions[0], currentDate.minusDays(7)),
        Pair(timeOptions[1], currentDate.minusDays(30)),
        Pair(timeOptions[2], LocalDate.of(currentDate.year, 1, 1)),
        Pair(
            timeOptions[3],
            uiState.history?.minBy { history -> history.date.toEpochDay() }?.date ?: currentDate
        ),
    )
    var detail by remember { mutableStateOf(detailOptions[0]) }
    var time by remember { mutableStateOf(timeOptions[0]) }
    ExerciseDetailsBestAndRecent(uiState)
    GraphOptions(
        detailOptions = detailOptions,
        detailOnChange = { newDetail -> detail = newDetail },
        timeOptions = timeOptions
    ) { newTime -> time = newTime }
    Graph(
        points = getGraphDetails(uiState, detail, detailOptions),
        startDate = timeOptionToStartTime[time] ?: currentDate,
        yLabel = detail,
        yUnit = if (detail == detailOptions[0] || detail == detailOptions[3]) " ${WeightUnits.KILOGRAMS.shortForm}" else ""
    )
}

@Composable
private fun ExerciseDetailsBestAndRecent(uiState: ExerciseDetailsUiState) {
    val bestWeight = uiState.history?.maxOf { history -> history.weight }
    val best = uiState.history
        ?.filter { history -> history.weight == bestWeight }
        ?.maxBy { history -> history.reps }
    val recent = uiState.history?.maxBy { history -> history.date.toEpochDay() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        ExerciseDetail(
            exerciseInfo = "${best?.weight} ${WeightUnits.KILOGRAMS.shortForm} for ${best?.reps} reps",
            iconId = R.drawable.trophy_48dp,
            iconDescription = "best exercise icon",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        ExerciseDetail(
            exerciseInfo = "${recent?.weight} ${WeightUnits.KILOGRAMS.shortForm} for ${recent?.reps} reps",
            iconId = R.drawable.history_48px,
            iconDescription = "recent exercise icon",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun ExerciseDetail(
    exerciseInfo: String,
    iconId: Int,
    iconDescription: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = iconDescription,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = exerciseInfo,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExerciseDetailsScreen(
            uiState = ExerciseDetailsUiState(
                name = "Curls",
                muscleGroup = "Biceps",
                equipment = "Dumbbells",
                history = listOf(
                    ExerciseHistoryUiState(
                        id = 1,
                        weight = 13.0,
                        sets = 1,
                        reps = 2,
                        rest = 1,
                        date = LocalDate.now().minusDays(5)
                    )
                )
            ),
            backNavigationFunction = { },
            updateFunction = { },
            deleteFunction = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreviewNoHistory() {
    GymTrackerTheme(darkTheme = false) {
        ExerciseDetailsScreen(
            uiState = ExerciseDetailsUiState(
                name = "Curls",
                muscleGroup = "Biceps",
                equipment = "Dumbbells",
                history = listOf()
            ),
            backNavigationFunction = { },
            updateFunction = { },
            deleteFunction = { }
        )
    }
}
