package com.askein.gymtracker.ui.exercise.history

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
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.enums.convertToDistanceUnit
import com.askein.gymtracker.enums.convertToKilometers
import com.askein.gymtracker.ui.DatePickerDialog
import com.askein.gymtracker.ui.DropdownBox
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.FormTimeField
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import java.time.LocalDate

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
    var cardioHistoryState by remember {
        mutableStateOf(
            savedHistory.toRecordCardioHistoryState(
                exerciseId,
                userPreferencesUiState.defaultDistanceUnit
            )
        )
    }
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
                minutes = cardioHistoryState.minutesState,
                seconds = cardioHistoryState.secondsState,
                minutesOnChange = { minutes ->
                    cardioHistoryState = cardioHistoryState.copy(minutesState = minutes)
                },
                secondsOnChange = { seconds ->
                    cardioHistoryState = cardioHistoryState.copy(secondsState = seconds)
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
                    value = cardioHistoryState.distanceState,
                    onChange = { distance ->
                        cardioHistoryState = cardioHistoryState.copy(distanceState = distance)
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
                    onChange = { unit ->
                        cardioHistoryState = cardioHistoryState.copy(unitState = unit)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                        .height(intrinsicSize = IntrinsicSize.Max)
                        .semantics { contentDescription = unitsContentDescription },
                    selected = cardioHistoryState.unitState
                )
            }
            FormInformationField(
                label = R.string.calories,
                value = cardioHistoryState.caloriesState,
                onChange = { calories ->
                    cardioHistoryState = cardioHistoryState.copy(caloriesState = calories)
                },
                formType = FormTypes.INTEGER,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            )
            if (savedHistory.workoutHistoryId == null) {
                DatePickerDialog(
                    date = cardioHistoryState.dateState,
                    onDateChange = { newDate ->
                        cardioHistoryState = cardioHistoryState.copy(dateState = newDate)
                    }
                )
            }
            SaveCardioExerciseHistoryButton(
                cardioHistoryState = cardioHistoryState,
                saveFunction = saveFunction,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun SaveCardioExerciseHistoryButton(
    cardioHistoryState: RecordCardioHistoryState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit
) {
    Button(onClick = {
        saveFunction(cardioHistoryState.toCardioExerciseHistoryUiState())
        onDismiss()
    }, enabled = cardioHistoryState.canSaveHistory()) {
        Text(stringResource(id = R.string.save))
    }
}

data class RecordCardioHistoryState(
    val historyId: Int,
    val exerciseId: Int,
    val workoutHistoryId: Int?,
    val minutesState: String,
    val secondsState: String,
    val caloriesState: String,
    val distanceState: String,
    val dateState: LocalDate,
    val unitState: DistanceUnits
)

fun RecordCardioHistoryState.canSaveHistory() = validTime() || validCalories() || validDistance()

fun RecordCardioHistoryState.validTime() = minutesState != "" && secondsState != "" && secondsState.toInt() < 60

fun RecordCardioHistoryState.validCalories() = caloriesState != ""

fun RecordCardioHistoryState.validDistance() = distanceState != ""

fun RecordCardioHistoryState.toCardioExerciseHistoryUiState() = CardioExerciseHistoryUiState(
    id = historyId,
    exerciseId = exerciseId,
    workoutHistoryId = workoutHistoryId,
    date = dateState,
    minutes = minutesState.toIntOrNull(),
    seconds = secondsState.toIntOrNull(),
    calories = caloriesState.toIntOrNull(),
    distance =  if (distanceState != "") convertToKilometers(unitState, distanceState.toDouble()) else null
)

fun CardioExerciseHistoryUiState.toRecordCardioHistoryState(
    exerciseId: Int,
    distanceUnit: DistanceUnits
) = RecordCardioHistoryState(
    historyId = id,
    exerciseId = exerciseId,
    workoutHistoryId = workoutHistoryId,
    minutesState = minutes?.toString() ?: "",
    secondsState = seconds?.toString() ?: "",
    caloriesState = calories?.toString() ?: "",
    distanceState = getDistanceForUnit(
        distance = distance,
        distanceUnit = distanceUnit
    ),
    dateState = date,
    unitState = distanceUnit,
)

private fun getDistanceForUnit(
    distance: Double?,
    distanceUnit: DistanceUnits
) = if (distance != null) {
    if (distanceUnit == DistanceUnits.KILOMETERS) {
        distance.toString()
    } else {
        convertToDistanceUnit(distanceUnit, distance).toString()
    }
} else {
    ""
}
