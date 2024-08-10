package com.askein.gymtracker.ui.workout.history.create.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.enums.convertToKilograms
import com.askein.gymtracker.enums.convertToWeightUnit
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.DropdownBox
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.FormTimeField
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.LocalUserPreferences

@Composable
fun LiveRecordWeightsExercise(
    uiState: ExerciseUiState,
    exerciseComplete: (WeightsExerciseHistoryUiState) -> Unit,
    exerciseCancel: () -> Unit,
    modifier: Modifier = Modifier,
    recordWeight: Boolean = true,
    viewModel: LiveRecordWeightsExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var recording by rememberSaveable { mutableStateOf(false) }
    var recordReps by rememberSaveable { mutableStateOf(true) }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp
        ),
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
                val defaultWeightUnit = LocalUserPreferences.current.defaultWeightUnit
                LiveRecordWeightsExerciseInfo(
                    onStart = { rest, recordingType ->
                        viewModel.setExerciseData(
                            exerciseId = uiState.id,
                            rest = rest,
                            recordReps = recordReps
                        )
                        viewModel.setUnitState(defaultWeightUnit)
                        recording = true
                        recordReps = recordingType
                    },
                    onCancel = { exerciseCancel() },
                )
            } else {
                val exerciseData = viewModel.exerciseState.collectAsState().value
                val timerState = viewModel.timerState.collectAsState().value
                val completedState = viewModel.completed.collectAsState().value
                val unitState = viewModel.unitState.collectAsState().value
                LiveRecordExerciseSetsAndTimer(
                    exerciseData = exerciseData,
                    recordReps = recordReps,
                    timerState = timerState,
                    timerFinishedState = completedState,
                    recordWeight = recordWeight,
                    unitState = unitState,
                    timerStart = { rest -> viewModel.startTimer(rest) },
                    addSetInfo = { reps, weight -> viewModel.addSetInfo(reps, weight) },
                    finishSet = { viewModel.finishSet() },
                    resetTimer = { viewModel.reset() },
                    setUnitState = { unit -> viewModel.setUnitState(unit) },
                    exerciseFinished = { exerciseComplete(exerciseData) }
                )
            }
        }
    }
}

@Composable
fun LiveRecordWeightsExerciseInfo(
    onStart: (Int, Boolean) -> Unit,
    onCancel: () -> Unit
) {
    var restState by rememberSaveable { mutableStateOf("") }
    var recordReps by rememberSaveable { mutableStateOf(true) }
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 0.dp)
        ) {
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
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Button(
                    onClick = { recordReps = true },
                    enabled = !recordReps
                ) {
                    Text(text = stringResource(id = R.string.reps))
                }
                Button(onClick = {
                    onStart(restState.toInt(), recordReps)
                }) {
                    Text(text = stringResource(id = R.string.start))
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Button(
                    onClick = { recordReps = false },
                    enabled = recordReps
                ) {
                    Text(text = stringResource(id = R.string.time))
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
}

@Composable
fun LiveRecordExerciseSetsAndTimer(
    exerciseData: WeightsExerciseHistoryUiState,
    recordReps: Boolean,
    timerState: TimerState,
    timerFinishedState: Boolean,
    recordWeight: Boolean,
    unitState: WeightUnits,
    timerStart: (Int) -> Unit,
    addSetInfo: (Int, Double) -> Unit,
    finishSet: () -> Unit,
    resetTimer: () -> Unit,
    setUnitState: (WeightUnits) -> Unit,
    exerciseFinished: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        var resting by rememberSaveable { mutableStateOf(false) }
        var showSetInfoForm by rememberSaveable { mutableStateOf(true) }
        Text(text = stringResource(id = R.string.sets_completed, exerciseData.sets))
        if (resting) {
            LaunchedEffect(Unit) {
                if (!timerState.timerRunning) {
                    timerStart(exerciseData.rest!!)
                }
            }
            if (timerState.currentTime >= 0) {
                Timer(
                    timerState = timerState.currentTime,
                    buttonEnabled = !showSetInfoForm
                ) {
                    resting = false
                }
            }
            resting = !timerFinishedState || showSetInfoForm
            if (showSetInfoForm) {
                AddSetRepsAndWeight(
                    exerciseData = exerciseData,
                    recordReps = recordReps,
                    recordWeight = recordWeight,
                    unitState = unitState,
                    addSetInfo = addSetInfo,
                    setUnitState = setUnitState,
                    onComplete = { showSetInfoForm = false }
                )
            }
        } else {
            resetTimer()
            Button(onClick = {
                finishSet()
                showSetInfoForm = true
                resting = true
            }) {
                Text(text = stringResource(id = R.string.finish_set))
            }
        }
        Button(onClick = { exerciseFinished() }) {
            Text(text = stringResource(id = R.string.finish_exercise))
        }
    }
}

@Composable
fun AddSetRepsAndWeight(
    exerciseData: WeightsExerciseHistoryUiState,
    recordReps: Boolean,
    recordWeight: Boolean,
    unitState: WeightUnits,
    addSetInfo: (Int, Double) -> Unit,
    setUnitState: (WeightUnits) -> Unit,
    onComplete: () -> Unit
) {
    var repsState by rememberSaveable {
        mutableStateOf((exerciseData.reps?.lastOrNull() ?: 0).toString())
    }
    var minutesState by rememberSaveable {
        mutableStateOf((exerciseData.seconds?.lastOrNull()?.div(60) ?: 0).toString())
    }
    var secondsState by rememberSaveable {
        mutableStateOf((exerciseData.seconds?.lastOrNull()?.mod(60) ?: 0).toString())
    }
    var weightsState by rememberSaveable {
        mutableStateOf(
            convertToWeightUnit(unitState, (exerciseData.weight.lastOrNull() ?: 0.0)).toString()
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (recordReps) {
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
            }
        } else {
            FormTimeField(
                minutes = minutesState,
                seconds = secondsState,
                minutesOnChange = { entry -> minutesState = entry },
                secondsOnChange = { entry -> secondsState = entry }
            )
        }
        if (recordWeight) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                FormInformationField(
                    label = R.string.weight,
                    value = weightsState,
                    onChange = { entry ->
                        weightsState = entry
                    },
                    formType = FormTypes.DOUBLE,
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                )
                val unitsContentDescription = stringResource(id = R.string.units)
                DropdownBox(
                    options = WeightUnits.values().associateWith { unit -> unit.shortForm },
                    onChange = { value ->
                        setUnitState(value)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                        .semantics { contentDescription = unitsContentDescription },
                    selected = unitState
                )
            }
        }
        val saveEnabled =
            (recordReps && repsState != "") || (!recordReps && minutesState != "" && secondsState != "") && (weightsState != "" || !recordWeight)
        Button(enabled = saveEnabled, onClick = {
            val repsOrSeconds = if (recordReps) {
                repsState.toInt()
            } else {
                minutesState.toInt() * 60 + secondsState.toInt()
            }
            addSetInfo(repsOrSeconds, convertToKilograms(unitState, weightsState.toDouble()))
            onComplete()
        }) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}

@Composable
fun Timer(
    timerState: Int,
    buttonEnabled: Boolean,
    finished: () -> Unit
) {
    Text(
        text = stringResource(
            id = R.string.rest_timer,
            String.format("%02d", timerState / 60),
            String.format("%02d", timerState % 60)
        )
    )
    Button(enabled = buttonEnabled, onClick = finished) {
        Text(text = stringResource(id = R.string.stop))
    }
}
