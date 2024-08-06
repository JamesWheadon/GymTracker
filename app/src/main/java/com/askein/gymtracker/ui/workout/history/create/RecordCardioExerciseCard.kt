package com.askein.gymtracker.ui.workout.history.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.FormTimeField
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.DistanceAndCalories
import com.askein.gymtracker.ui.exercise.history.state.record.RecordCardioHistoryState

@Composable
fun RecordCardioExerciseCard(
    exercise: ExerciseUiState,
    recordCardioHistory: RecordCardioHistoryState?,
    recordCardioHistoryOnChange: (RecordCardioHistoryState) -> Unit,
    selectExerciseFunction: () -> Unit,
    deselectExerciseFunction: () -> Unit
) {
    var expanded by remember { mutableStateOf(recordCardioHistory != null) }
    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
            if (recordCardioHistory != null) {
                RecordWorkoutCardioExerciseHistory(
                    recordCardioHistory = recordCardioHistory,
                    recordCardioHistoryOnChange = recordCardioHistoryOnChange
                )
            }
        }
    }
}

@Composable
private fun RecordWorkoutCardioExerciseHistory(
    recordCardioHistory: RecordCardioHistoryState,
    recordCardioHistoryOnChange: (RecordCardioHistoryState) -> Unit
) {
    FormTimeField(
        minutes = recordCardioHistory.minutesState,
        seconds = recordCardioHistory.secondsState,
        minutesOnChange = { minutes ->
            recordCardioHistoryOnChange(recordCardioHistory.copy(minutesState = minutes))
        },
        secondsOnChange = { seconds ->
            recordCardioHistoryOnChange(recordCardioHistory.copy(secondsState = seconds))
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 0.dp)
    )
    DistanceAndCalories(
        recordCardioHistory = recordCardioHistory,
        recordCardioHistoryOnChange = recordCardioHistoryOnChange
    )
    if (!recordCardioHistory.isValid()) {
        Text(
            text = stringResource(id = R.string.cardio_error),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 0.dp)
        )
    }
}
