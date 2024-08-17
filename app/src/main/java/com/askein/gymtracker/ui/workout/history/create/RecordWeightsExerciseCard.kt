package com.askein.gymtracker.ui.workout.history.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.InputSetDetails
import com.askein.gymtracker.ui.exercise.history.RepsOrTimeSelector
import com.askein.gymtracker.ui.exercise.history.SelectsSetsAndUnits
import com.askein.gymtracker.ui.exercise.history.state.record.RecordWeightsHistoryState

@Composable
fun RecordWeightsExerciseCard(
    exercise: ExerciseUiState,
    recordWeightsHistory: RecordWeightsHistoryState?,
    recordWeightsHistoryOnChange: (RecordWeightsHistoryState) -> Unit,
    selectExerciseFunction: () -> Unit,
    deselectExerciseFunction: () -> Unit
) {
    var expanded by remember { mutableStateOf(recordWeightsHistory != null) }
    Card {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = exercise.name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.Center),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Checkbox(
                    checked = expanded,
                    onCheckedChange = {
                        if (expanded) {
                            deselectExerciseFunction()
                        } else {
                            selectExerciseFunction()
                        }
                        expanded = !expanded
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            if (recordWeightsHistory != null) {
                RecordWorkoutWeightsExerciseHistory(
                    recordWeightsHistory = recordWeightsHistory,
                    recordWeightsHistoryOnChange = recordWeightsHistoryOnChange
                )
            }
        }
    }
}

@Composable
private fun RecordWorkoutWeightsExerciseHistory(
    recordWeightsHistory: RecordWeightsHistoryState,
    recordWeightsHistoryOnChange: (RecordWeightsHistoryState) -> Unit
) {
    var showSetDetails by remember { mutableStateOf(true) }
    SelectsSetsAndUnits(
        recordWeightsHistory = recordWeightsHistory,
        recordWeightsHistoryOnChange = recordWeightsHistoryOnChange
    )
    RepsOrTimeSelector(
        recordWeightsHistory = recordWeightsHistory,
        recordWeightsHistoryOnChange = recordWeightsHistoryOnChange
    )
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {
            showSetDetails = !showSetDetails
        }) {
            Icon(
                imageVector = if (showSetDetails) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(id = R.string.show_hide_sets)
            )
        }
    }
    AnimatedVisibility(visible = showSetDetails) {
        Column {
            InputSetDetails(recordWeightsHistory = recordWeightsHistory)
        }
    }
}
