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
import androidx.compose.material3.CardDefaults
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
import com.askein.gymtracker.ui.DatePickerDialog
import com.askein.gymtracker.ui.DropdownBox
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.FormTimeField
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.record.RecordCardioHistoryState
import com.askein.gymtracker.ui.exercise.history.state.record.toRecordCardioHistoryState
import com.askein.gymtracker.ui.user.LocalUserPreferences

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
    var recordCardioHistory by remember {
        mutableStateOf(
            savedHistory.toRecordCardioHistoryState(
                exerciseId = exerciseId,
                distanceUnit = userPreferencesUiState.defaultDistanceUnit
            )
        )
    }
    Card(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp
        )
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
                minutes = recordCardioHistory.minutesState,
                seconds = recordCardioHistory.secondsState,
                minutesOnChange = { minutes ->
                    recordCardioHistory = recordCardioHistory.copy(minutesState = minutes)
                },
                secondsOnChange = { seconds ->
                    recordCardioHistory = recordCardioHistory.copy(secondsState = seconds)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            )
            DistanceAndCalories(
                recordCardioHistory = recordCardioHistory,
                recordCardioHistoryOnChange = { newState -> recordCardioHistory = newState }
            )
            if (savedHistory.workoutHistoryId == null) {
                DatePickerDialog(
                    date = recordCardioHistory.dateState,
                    onDateChange = { newDate ->
                        recordCardioHistory = recordCardioHistory.copy(dateState = newDate)
                    }
                )
            }
            SaveCardioExerciseHistoryButton(
                recordCardioHistory = recordCardioHistory,
                saveFunction = saveFunction,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun DistanceAndCalories(
    recordCardioHistory: RecordCardioHistoryState,
    recordCardioHistoryOnChange: (RecordCardioHistoryState) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 0.dp)
    ) {
        FormInformationField(
            label = R.string.distance,
            value = recordCardioHistory.distanceState,
            onChange = { distance ->
                recordCardioHistoryOnChange(recordCardioHistory.copy(distanceState = distance))
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
                recordCardioHistoryOnChange(recordCardioHistory.copy(unitState = unit))
            },
            modifier = Modifier
                .weight(1f)
                .padding(0.dp)
                .height(intrinsicSize = IntrinsicSize.Max)
                .semantics { contentDescription = unitsContentDescription },
            selected = recordCardioHistory.unitState
        )
    }
    FormInformationField(
        label = R.string.calories,
        value = recordCardioHistory.caloriesState,
        onChange = { calories ->
            recordCardioHistoryOnChange(recordCardioHistory.copy(caloriesState = calories))
        },
        formType = FormTypes.INTEGER,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 0.dp)
    )
}

@Composable
fun SaveCardioExerciseHistoryButton(
    recordCardioHistory: RecordCardioHistoryState,
    saveFunction: (ExerciseHistoryUiState) -> Unit,
    onDismiss: () -> Unit
) {
    Button(onClick = {
        saveFunction(recordCardioHistory.toHistoryUiState())
        onDismiss()
    }, enabled = recordCardioHistory.isValid()) {
        Text(stringResource(id = R.string.save))
    }
}
