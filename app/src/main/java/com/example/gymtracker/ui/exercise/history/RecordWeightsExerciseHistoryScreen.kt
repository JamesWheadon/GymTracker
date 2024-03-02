package com.example.gymtracker.ui.exercise.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymtracker.converters.WeightUnits
import com.example.gymtracker.converters.convertToKilograms
import com.example.gymtracker.converters.convertToWeightUnit
import com.example.gymtracker.converters.getWeightUnitFromShortForm
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.toWeightsExerciseHistory
import com.example.gymtracker.ui.exercise.history.state.toWeightsExerciseHistoryUiState
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState
import java.time.LocalDate

@Composable
fun RecordWeightsExerciseHistoryCard(
    exerciseId: Int,
    cardTitle: String,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    savedHistory: WeightsExerciseHistoryUiState = WeightsExerciseHistoryUiState()
) {
    val userPreferencesUiState = LocalUserPreferences.current
    var setsState by remember { mutableStateOf(if (savedHistory == WeightsExerciseHistoryUiState()) "" else savedHistory.sets.toString()) }
    var repsState by remember { mutableStateOf(if (savedHistory == WeightsExerciseHistoryUiState()) "" else savedHistory.reps.toString()) }
    var weightState by remember {
        mutableStateOf(
            if (savedHistory == WeightsExerciseHistoryUiState()) "" else getWeightForUnit(
                savedHistory,
                userPreferencesUiState
            )
        )
    }
    var unitState by remember { mutableStateOf(userPreferencesUiState.defaultWeightUnit.shortForm) }
    Card(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        elevation = customCardElevation()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cardTitle
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                FormInformationField(
                    label = "Sets",
                    value = setsState,
                    onChange = { entry ->
                        setsState = entry
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
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
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top,
                modifier = modifier
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
                Spacer(modifier = Modifier.width(12.dp))
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
                        .semantics { contentDescription = "Units" },
                    selected = userPreferencesUiState.defaultWeightUnit.shortForm
                )
            }
            SaveWeightsExerciseHistoryButton(
                setsState = setsState,
                repsState = repsState,
                weightState = weightState,
                unitState = unitState,
                exerciseId = exerciseId,
                savedHistory = savedHistory,
                saveFunction = saveFunction,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun SaveWeightsExerciseHistoryButton(
    setsState: String,
    repsState: String,
    weightState: String,
    unitState: String,
    exerciseId: Int,
    savedHistory: WeightsExerciseHistoryUiState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit
) {
    if (setsState != "" && repsState != "" && weightState != "" && unitState != "") {
        val weight = weightState.toDouble()
        val unit = getWeightUnitFromShortForm(unitState)
        val history = if (savedHistory == WeightsExerciseHistoryUiState()) {
            WeightsExerciseHistory(
                exerciseId = exerciseId,
                weight = convertToKilograms(unit, weight),
                sets = setsState.toInt(),
                reps = repsState.toInt(),
                date = LocalDate.now()
            )
        } else {
            savedHistory.sets = setsState.toInt()
            savedHistory.reps = repsState.toInt()
            savedHistory.weight = convertToKilograms(unit, weight)
            savedHistory.toWeightsExerciseHistory(exerciseId)
        }
        Button(onClick = {
            saveFunction(history.toWeightsExerciseHistoryUiState())
            onDismiss()
        }) {
            Text("Save")
        }
    } else {
        Button(
            onClick = { },
            enabled = false
        ) {
            Text("Save")
        }
    }
}

private fun getWeightForUnit(
    exerciseHistory: WeightsExerciseHistoryUiState,
    userPreferencesUiState: UserPreferencesUiState
) =
    if (userPreferencesUiState.defaultWeightUnit == WeightUnits.KILOGRAMS) {
        exerciseHistory.weight.toString()
    } else {
        convertToWeightUnit(
            userPreferencesUiState.defaultWeightUnit,
            exerciseHistory.weight
        ).toString()
    }

@Preview(showBackground = true)
@Composable
fun RecordExerciseHistoryScreenPreview() {
    val userPreferencesUiState = UserPreferencesUiState()
    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
        GymTrackerTheme(darkTheme = false) {
            RecordWeightsExerciseHistoryCard(
                exerciseId = 1,
                cardTitle = "",
                saveFunction = {},
                onDismiss = {}
            )
        }
    }
}
