package com.askein.gymtracker.ui.workout.history.create.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.enums.convertToKilograms
import com.askein.gymtracker.ui.DropdownBox
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import com.askein.gymtracker.ui.user.LocalUserPreferences
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun LiveRecordWeightsExercise(
    uiState: ExerciseUiState,
    exerciseComplete: (WeightsExerciseHistoryUiState) -> Unit,
    exerciseCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var recording by remember { mutableStateOf(false) }
    val exerciseData = WeightsExerciseHistoryUiState(exerciseId = uiState.id)
    Card(
        elevation = customCardElevation(),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = uiState.name,
                style = MaterialTheme.typography.headlineSmall
            )
            if (!recording) {
                LiveRecordWeightsExerciseInfo(
                    onStart = { data ->
                        exerciseData.rest = data.rest
                        exerciseData.reps = data.reps
                        exerciseData.weight = data.weight
                        recording = true
                    },
                    onCancel = { exerciseCancel() }
                )
            } else {
                LiveRecordExerciseSetsAndTimer(
                    exerciseData = exerciseData
                ) { setsCompleted ->
                    exerciseData.sets = setsCompleted
                    exerciseComplete(exerciseData)
                }
            }
        }
    }
}

@Composable
fun LiveRecordWeightsExerciseInfo(
    onStart: (ExerciseData) -> Unit,
    onCancel: () -> Unit
) {
    val userPreferencesUiState = LocalUserPreferences.current
    var repsState by rememberSaveable { mutableStateOf("") }
    var restState by rememberSaveable { mutableStateOf("") }
    var weightState by rememberSaveable { mutableStateOf("") }
    var unitState by rememberSaveable { mutableStateOf(userPreferencesUiState.defaultWeightUnit) }
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 0.dp)
        ) {
            FormInformationField(
                label = R.string.reps,
                value = repsState,
                onChange = { entry ->
                    repsState = entry
                },
                formType = FormTypes.INTEGER,
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
            )
            FormInformationField(
                label = R.string.rest,
                value = restState,
                onChange = { entry ->
                    restState = entry
                },
                formType = FormTypes.INTEGER,
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 0.dp)
        ) {
            FormInformationField(
                label = R.string.weight,
                value = weightState,
                onChange = { entry ->
                    weightState = entry
                },
                formType = FormTypes.DOUBLE,
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
            )
            val unitsContentDescription = stringResource(id = R.string.units)
            DropdownBox(
                options = WeightUnits.values().associateWith { it.shortForm },
                onChange = { value ->
                    unitState = value
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
                    .semantics { contentDescription = unitsContentDescription },
                selected = unitState
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                onStart(
                    ExerciseData(
                        reps = repsState.toInt(),
                        rest = restState.toInt(),
                        weight = if (unitState == WeightUnits.KILOGRAMS) {
                            weightState.toDouble()
                        } else {
                            convertToKilograms(
                                unitState,
                                weightState.toDouble()
                            )
                        }
                    )
                )
            }) {
                Text(text = stringResource(id = R.string.start))
            }
            Button(
                onClick = {
                    onCancel()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    }
}

@Composable
fun LiveRecordExerciseSetsAndTimer(
    exerciseData: WeightsExerciseHistoryUiState,
    exerciseFinished: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        var setsComplete by rememberSaveable { mutableIntStateOf(0) }
        var resting by rememberSaveable { mutableStateOf(false) }
        Text(text = stringResource(id = R.string.sets_completed, setsComplete))
        if (resting) {
            Timer(rest = exerciseData.rest!!) {
                resting = false
            }
        } else {
            Button(onClick = {
                setsComplete++
                resting = true
            }) {
                Text(text = stringResource(id = R.string.finish_set))
            }
        }
        Button(onClick = { exerciseFinished(setsComplete) }) {
            Text(text = stringResource(id = R.string.finish_exercise))
        }
    }
}

@Composable
fun Timer(
    rest: Int,
    finished: () -> Unit
) {
    var time by rememberSaveable { mutableIntStateOf(rest) }
    LaunchedEffect(Unit) {
        while (time > 0) {
            delay(1.seconds)
            time--
        }
        finished()
    }
    Text(
        text = stringResource(
            id = R.string.rest_timer,
            String.format("%02d", time / 60),
            String.format("%02d", time % 60)
        )
    )
    Button(onClick = finished) {
        Text(text = stringResource(id = R.string.stop))
    }
}

data class ExerciseData(val reps: Int = 0, val rest: Int = 0, val weight: Double = 0.0)

@Preview(showBackground = true)
@Composable
fun LiveRecordWeightsExercisePreview() {
    GymTrackerTheme(darkTheme = false) {
        LiveRecordWeightsExercise(
            uiState = ExerciseUiState(1, "Curls", "Biceps", "Dumbbells"),
            exerciseComplete = { },
            exerciseCancel = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LiveRecordExerciseSetsAndTimerPreview() {
    GymTrackerTheme(darkTheme = false) {
        LiveRecordExerciseSetsAndTimer(
            exerciseData = WeightsExerciseHistoryUiState(),
            exerciseFinished = { }
        )
    }
}
