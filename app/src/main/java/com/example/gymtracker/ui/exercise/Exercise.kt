package com.example.gymtracker.ui.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymtracker.R
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.theme.GymTrackerTheme


@Composable
fun ExerciseCard(
    exercise: ExerciseUiState,
    navigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (exercise.equipment == "") {
        CardioExerciseCard(
            exercise = exercise,
            navigationFunction = navigationFunction,
            modifier = modifier
        )
    } else {
        WeightsExerciseCard(
            exercise = exercise,
            navigationFunction = navigationFunction,
            modifier = modifier
        )
    }
}

@Composable
fun WeightsExerciseCard(
    exercise: ExerciseUiState,
    navigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RectangleShape,
        onClick = { navigationFunction(exercise.id) }
    ) {
        Card(
            modifier = modifier,
            elevation = customCardElevation()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(0.55f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExerciseDetail(
                        exerciseInfo = exercise.muscleGroup,
                        iconId = R.drawable.info_48px,
                        iconDescription = "muscle icon"
                    )
                    ExerciseDetail(
                        exerciseInfo = exercise.equipment,
                        iconId = R.drawable.exercise_filled_48px,
                        iconDescription = "equipment icon"
                    )
                }
            }
        }
    }
}

@Composable
fun CardioExerciseCard(
    exercise: ExerciseUiState,
    navigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RectangleShape,
        onClick = { navigationFunction(exercise.id) }
    ) {
        Card(
            modifier = modifier,
            elevation = customCardElevation()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(0.55f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                ExerciseDetail(
                    exerciseInfo = "Cardio",
                    iconId = R.drawable.cardio_48dp,
                    iconDescription = "cardio icon",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ExerciseDetail(
    exerciseInfo: String,
    iconId: Int,
    iconDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = iconDescription,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = exerciseInfo,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CardioExerciseCardPreview() {
    GymTrackerTheme(darkTheme = false) {
        CardioExerciseCard(
            exercise = ExerciseUiState(0, "Curls"),
            navigationFunction = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeightsExerciseCardPreview() {
    GymTrackerTheme(darkTheme = false) {
        WeightsExerciseCard(
            exercise = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells"),
            navigationFunction = { }
        )
    }
}
