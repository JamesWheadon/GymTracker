package com.example.gymtracker.ui.workout.history.create.live

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymtracker.converters.WeightUnits
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun LiveRecordExercise(
    uiState: ExerciseUiState,
    exerciseComplete: (ExerciseHistoryUiState) -> Unit,
    exerciseCancel: () -> Unit
) {
    var recording by remember { mutableStateOf(false) }
    val exerciseData = ExerciseHistoryUiState(exerciseId = uiState.id)
    Card(
        elevation = customCardElevation()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = uiState.name)
            if (!recording) {
                LiveRecordExerciseInfo(
                    onStart = { data ->
                        exerciseData.rest = data.rest
                        exerciseData.reps = data.reps
                        exerciseData.weight = data.weight
                        recording = true
                    },
                    onCancel = { exerciseCancel() }
                )
            } else {
                Log.i("LiveRecordExercise", exerciseData.toString())
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
fun LiveRecordExerciseInfo(
    onStart: (ExerciseData) -> Unit,
    onCancel: () -> Unit
) {
    var repsState by rememberSaveable { mutableStateOf("") }
    var restState by rememberSaveable { mutableStateOf("") }
    var weightState by rememberSaveable { mutableStateOf("") }
    var unitState by rememberSaveable { mutableStateOf(WeightUnits.KILOGRAMS.shortForm) }
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 0.dp)
        ) {
            FormInformationField(
                label = "Reps",
                value = repsState,
                onChange = { entry ->
                    repsState = entry
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
            )
            FormInformationField(
                label = "Rest",
                value = restState,
                onChange = { entry ->
                    restState = entry
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 0.dp)
        ) {
            FormInformationField(
                label = "Weight",
                value = weightState,
                onChange = { entry ->
                    weightState = entry
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
            )
            DropdownBox(
                options = listOf(
                    WeightUnits.KILOGRAMS.shortForm,
                    WeightUnits.POUNDS.shortForm
                ),
                onChange = { value ->
                    unitState = value
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
                    .semantics { contentDescription = "Units" }
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
                        weight = weightState.toDouble()
                    )
                )
            }) {
                Text(text = "Start")
            }
            Button(
                onClick = {
                    onCancel()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
fun LiveRecordExerciseSetsAndTimer(
    exerciseData: ExerciseHistoryUiState,
    exerciseFinished: (Int) -> Unit
) {
    Column {
        var setsComplete by rememberSaveable { mutableStateOf(0) }
        var resting by rememberSaveable { mutableStateOf(false) }
        Text(text = "Sets Completed: $setsComplete")
        if (resting) {
            Timer(rest = exerciseData.rest) {
                resting = false
            }
        } else {
            Button(onClick = {
                setsComplete++
                resting = true
            }) {
                Text(text = "Finish Set")
            }
        }
        Button(onClick = { exerciseFinished(setsComplete) }) {
            Text(text = "Finish Exercise")
        }
    }
}

@Composable
fun Timer(
    rest: Int,
    finished: () -> Unit
) {
    var time by rememberSaveable { mutableStateOf(rest) }
    LaunchedEffect(Unit) {
        while (time > 0) {
            delay(1.seconds)
            time--
        }
        finished()
    }
    Text(text = String.format("%02d:%02d", time / 60, time % 60))
    Button(onClick = finished) {
        Text(text = "Stop")
    }
}

data class ExerciseData(val reps: Int = 0, val rest: Int = 0, val weight: Double = 0.0)

@Preview(showBackground = true)
@Composable
fun LiveRecordExercisePreview() {
    GymTrackerTheme(darkTheme = false) {
        LiveRecordExercise(
            uiState = ExerciseUiState(1, "Curls", "Biceps", "Dumbbells"),
            exerciseComplete = { },
            exerciseCancel = { }
        )
    }
}
