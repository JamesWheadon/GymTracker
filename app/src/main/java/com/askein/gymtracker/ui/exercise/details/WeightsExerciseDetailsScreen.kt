package com.askein.gymtracker.ui.exercise.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.convertToWeightUnit
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import com.askein.gymtracker.util.getTimeStringResourceFromSeconds
import java.time.LocalDate

@Composable
fun WeightsExerciseDetailsScreen(
    innerPadding: PaddingValues,
    uiState: ExerciseDetailsUiState,
    chosenDate: LocalDate?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        WeightsExerciseInformation(innerPadding, uiState)
        if (uiState.weightsHistory.isNotEmpty()) {
            WeightsExerciseHistoryDetails(uiState = uiState)
            ExerciseHistoryCalendar(
                uiState = uiState,
                chosenDate = chosenDate
            )
        }
        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
private fun WeightsExerciseInformation(
    innerPadding: PaddingValues,
    uiState: ExerciseDetailsUiState
) {
    if (uiState.exercise.muscleGroup != "" || uiState.exercise.equipment != "") {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            if (uiState.exercise.muscleGroup != "") {
                ExerciseDetail(
                    exerciseInfo = uiState.exercise.muscleGroup,
                    iconId = R.drawable.info_48px,
                    iconDescription = R.string.muscle_icon,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            if (uiState.exercise.equipment != "") {
                ExerciseDetail(
                    exerciseInfo = uiState.exercise.equipment,
                    iconId = R.drawable.exercise_filled_48px,
                    iconDescription = R.string.equipment_icon,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    } else {
        ExerciseDetail(
            exerciseInfo = stringResource(id = R.string.weights),
            iconId = R.drawable.exercise_filled_48px,
            iconDescription = R.string.equipment_icon,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        )
    }
}

@Composable
fun WeightsExerciseHistoryDetails(
    uiState: ExerciseDetailsUiState
) {
    val (detailOptions, timeOptionToStartTime) = graphOptionsForWeightsExercise(uiState)
    var detail by remember { mutableIntStateOf(detailOptions[0]) }
    var time by remember { mutableIntStateOf(timeOptionToStartTime.keys.first()) }
    WeightsExerciseDetailsBestAndRecent(uiState)
    GraphOptions(
        detailOptions = detailOptions,
        detailOnChange = { newDetail -> detail = newDetail },
        timeOptions = timeOptionToStartTime.keys.toList(),
        timeOnChange = { newTime -> time = newTime }
    )
    val weightUnit = LocalUserPreferences.current.defaultWeightUnit
    val dataPoints = calisthenicsAndWeightsGraphDataPoints(
        historyUiStates = uiState.weightsHistory,
        startDate = timeOptionToStartTime[time]!!,
        chosenDetail = detail,
        weightUnit = weightUnit
    )
    if (dataPoints.isNotEmpty()) {
        val yUnit = when (detail) {
            R.string.max_weight, R.string.total_weight -> stringResource(
                id = weightUnit.shortForm
            )

            R.string.max_time, R.string.total_time -> stringResource(
                id = R.string.seconds_unit
            )

            else -> ""
        }
        Graph(
            points = dataPoints,
            startDate = timeOptionToStartTime[time]!!,
            yLabel = stringResource(id = detail),
            yUnit = yUnit
        )
    } else {
        Text(text = stringResource(id = R.string.no_data_error))
    }
}

@Composable
private fun WeightsExerciseDetailsBestAndRecent(
    uiState: ExerciseDetailsUiState
) {
    val userPreferencesUiState = LocalUserPreferences.current
    val bestReps = bestRepsForWeightsExercise(
        uiState.weightsHistory,
        userPreferencesUiState.displayHighestWeight
    )
    val bestTime = bestTimeForWeightsExercise(
        uiState.weightsHistory,
        userPreferencesUiState.displayHighestWeight
    )
    val recent = uiState.weightsHistory.maxBy { history -> history.date.toEpochDay() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (bestReps != null) {
            ExerciseDetail(
                exerciseInfo = stringResource(
                    id = R.string.weights_exercise_reps,
                    convertToWeightUnit(userPreferencesUiState.defaultWeightUnit, bestReps.first),
                    stringResource(id = userPreferencesUiState.defaultWeightUnit.shortForm),
                    bestReps.second
                ),
                iconId = R.drawable.trophy_48dp,
                iconDescription = R.string.best_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
        if (bestTime != null) {
            val (resourceId, resourceArgs) = getTimeStringResourceFromSeconds(bestTime.second)
            ExerciseDetail(
                exerciseInfo = stringResource(
                    id = R.string.weights_exercise_time,
                    convertToWeightUnit(userPreferencesUiState.defaultWeightUnit, bestTime.first),
                    stringResource(id = userPreferencesUiState.defaultWeightUnit.shortForm),
                    stringResource(id = resourceId, *resourceArgs.toTypedArray())
                ),
                iconId = R.drawable.trophy_48dp,
                iconDescription = R.string.best_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
        if (recent.reps == null) {
            val (resourceId, resourceArgs) = getTimeStringResourceFromSeconds(recent.seconds!!.last())
            ExerciseDetail(
                exerciseInfo = stringResource(
                    id = R.string.weights_exercise_time,
                    convertToWeightUnit(
                        userPreferencesUiState.defaultWeightUnit,
                        recent.weight.last()
                    ),
                    stringResource(id = userPreferencesUiState.defaultWeightUnit.shortForm),
                    stringResource(id = resourceId, *resourceArgs.toTypedArray())
                ),
                iconId = R.drawable.history_48px,
                iconDescription = R.string.recent_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            ExerciseDetail(
                exerciseInfo = stringResource(
                    id = R.string.weights_exercise_reps,
                    convertToWeightUnit(
                        userPreferencesUiState.defaultWeightUnit,
                        recent.weight.last()
                    ),
                    stringResource(id = userPreferencesUiState.defaultWeightUnit.shortForm),
                    recent.reps!!.last()
                ),
                iconId = R.drawable.history_48px,
                iconDescription = R.string.recent_exercise_icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

fun bestRepsForWeightsExercise(
    exerciseHistory: List<WeightsExerciseHistoryUiState>,
    displayHighestWeight: Boolean
) = if (exerciseHistory.any { history -> history.reps != null }) {
    val flattenedHistoryRepsWeights = exerciseHistory
        .filter { history -> history.reps != null }
        .map { history -> history.weight.zip(history.reps!!) }
        .flatten()
    if (displayHighestWeight) {
        flattenedHistoryRepsWeights
            .maxWith(compareBy({ it.first }, { it.second }))
    } else {
        flattenedHistoryRepsWeights
            .maxBy { it.first * it.second }
    }
} else {
    null
}

fun bestTimeForWeightsExercise(
    exerciseHistory: List<WeightsExerciseHistoryUiState>,
    displayHighestWeight: Boolean
) = if (exerciseHistory.any { history -> history.seconds != null }) {
    val flattenedHistoryRepsWeights = exerciseHistory
        .filter { history -> history.seconds != null }
        .map { history -> history.weight.zip(history.seconds!!) }
        .flatten()
    if (displayHighestWeight) {
        flattenedHistoryRepsWeights
            .maxWith(compareBy({ it.first }, { it.second }))
    } else {
        flattenedHistoryRepsWeights
            .maxBy { it.first * it.second }
    }
} else {
    null
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreviewNoHistory() {
    val userPreferencesUiState = UserPreferencesUiState()
    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
        GymTrackerTheme(darkTheme = false) {
            WeightsExerciseDetailsScreen(
                innerPadding = PaddingValues(),
                uiState = ExerciseDetailsUiState(
                    exercise = ExerciseUiState(
                        name = "Curls",
                        muscleGroup = "Biceps",
                        equipment = "Dumbbells"
                    ),
                    weightsHistory = listOf()
                ),
                chosenDate = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreviewHistory() {
    val userPreferencesUiState = UserPreferencesUiState()
    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
        GymTrackerTheme(darkTheme = false) {
            WeightsExerciseDetailsScreen(
                innerPadding = PaddingValues(),
                uiState = ExerciseDetailsUiState(
                    exercise = ExerciseUiState(
                        name = "Curls",
                        muscleGroup = "Biceps",
                        equipment = "Dumbbells And BenchPress"
                    ),
                    weightsHistory = listOf(
                        WeightsExerciseHistoryUiState(
                            id = 1,
                            weight = listOf(13.0, 12.5),
                            sets = 1,
                            reps = listOf(2, 4),
                            rest = 1,
                            date = LocalDate.now().minusDays(5)
                        )
                    )
                ),
                chosenDate = null
            )
        }
    }
}
