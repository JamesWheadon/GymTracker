package com.example.gymtracker.ui.exercise.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.gymtracker.R
import com.example.gymtracker.enums.DistanceUnits
import com.example.gymtracker.enums.FormTypes
import com.example.gymtracker.enums.convertToDistanceUnit
import com.example.gymtracker.enums.convertToKilometers
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.FormInformationField
import com.example.gymtracker.ui.FormTimeField
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.example.gymtracker.ui.user.LocalUserPreferences
import com.example.gymtracker.ui.user.UserPreferencesUiState

@Composable
fun RecordCardioExerciseHistoryCard(
    exerciseId: Int,
    cardTitle: String,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    savedHistory: CardioExerciseHistoryUiState = CardioExerciseHistoryUiState()
) {
    val userPreferencesUiState = LocalUserPreferences.current
    var minutesState by remember { mutableStateOf(savedHistory.minutes?.toString() ?: "") }
    var secondsState by remember { mutableStateOf(savedHistory.seconds?.toString() ?: "") }
    var caloriesState by remember { mutableStateOf(savedHistory.calories?.toString() ?: "") }
    var distanceState by remember {
        mutableStateOf(
            getDistanceForUnit(
                savedHistory,
                userPreferencesUiState
            )
        )
    }
    var unitState by remember { mutableStateOf(userPreferencesUiState.defaultDistanceUnit) }
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
            FormTimeField(
                minutes = minutesState,
                seconds = secondsState,
                minutesOnChange = { minutes ->
                    minutesState = minutes
                },
                secondsOnChange = { seconds ->
                    secondsState = seconds
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                FormInformationField(
                    label = R.string.distance,
                    value = distanceState,
                    onChange = { distance ->
                        distanceState = distance
                    },
                    formType = FormTypes.DOUBLE,
                    modifier = Modifier
                        .weight(1f)
                        .height(intrinsicSize = IntrinsicSize.Max)
                        .padding(0.dp)
                )
                val unitsContentDescription = stringResource(id = R.string.units)
                DropdownBox(
                    options = DistanceUnits.values().associateWith { unit -> unit.shortForm },
                    onChange = { value ->
                        unitState = value
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                        .height(intrinsicSize = IntrinsicSize.Max)
                        .semantics { contentDescription = unitsContentDescription },
                    selected = unitState
                )
            }
            FormInformationField(
                label = R.string.calories,
                value = caloriesState,
                onChange = { calories ->
                    caloriesState = calories
                },
                formType = FormTypes.INTEGER,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            )
            SaveCardioExerciseHistoryButton(
                minutesState = minutesState,
                secondsState = secondsState,
                caloriesState = caloriesState,
                distanceState = distanceState,
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
fun SaveCardioExerciseHistoryButton(
    minutesState: String,
    secondsState: String,
    caloriesState: String,
    distanceState: String,
    unitState: DistanceUnits,
    exerciseId: Int,
    savedHistory: CardioExerciseHistoryUiState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit
) {
    val enabled =
        (minutesState != "" && secondsState != "" && secondsState.toInt() < 60) || caloriesState != "" || distanceState != ""
    Button(onClick = {
        val history = createHistory(
            distanceState,
            unitState,
            minutesState,
            secondsState,
            savedHistory,
            exerciseId,
            caloriesState
        )
        saveFunction(history)
        onDismiss()
    }, enabled = enabled) {
        Text(stringResource(id = R.string.save))
    }
}

private fun createHistory(
    distanceState: String,
    unitState: DistanceUnits,
    minutesState: String,
    secondsState: String,
    savedHistory: CardioExerciseHistoryUiState,
    exerciseId: Int,
    caloriesState: String
): CardioExerciseHistoryUiState {
    val distance = if (distanceState == "") {
        null
    } else {
        convertToKilometers(unitState, distanceState.toDouble())
    }
    val minutes = minutesState.toIntOrNull()
    val seconds = secondsState.toIntOrNull()
    return if (savedHistory == CardioExerciseHistoryUiState()) {
        CardioExerciseHistoryUiState(
            exerciseId = exerciseId,
            distance = distance,
            minutes = minutes,
            seconds = seconds,
            calories = caloriesState.toIntOrNull()
        )
    } else {
        savedHistory.distance = distance
        savedHistory.minutes = minutes
        savedHistory.seconds = seconds
        savedHistory.calories = caloriesState.toIntOrNull()
        savedHistory
    }
}

private fun getDistanceForUnit(
    exerciseHistory: CardioExerciseHistoryUiState,
    userPreferencesUiState: UserPreferencesUiState
) = if (exerciseHistory.distance != null) {
    if (userPreferencesUiState.defaultDistanceUnit == DistanceUnits.KILOMETERS) {
        exerciseHistory.distance.toString()
    } else {
        convertToDistanceUnit(
            userPreferencesUiState.defaultDistanceUnit,
            exerciseHistory.distance!!
        ).toString()
    }
} else {
    ""
}
