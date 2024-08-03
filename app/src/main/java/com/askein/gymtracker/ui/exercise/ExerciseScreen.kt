package com.askein.gymtracker.ui.exercise

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import java.time.LocalDate

@Composable
fun ExerciseCard(
    exercise: ExerciseUiState,
    navigationFunction: (Int, LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
    chosenDate: LocalDate? = null
) {
    Button(
        shape = RectangleShape,
        onClick = { navigationFunction(exercise.id, chosenDate) },
        contentPadding = PaddingValues(12.dp)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            )
        ) {
            when (exercise.type) {
                ExerciseType.WEIGHTS -> {
                    WeightsExerciseCard(
                        exercise = exercise
                    )
                }

                ExerciseType.CARDIO -> {
                    CardioExerciseCard(
                        exercise = exercise
                    )
                }

                ExerciseType.CALISTHENICS -> {
                    CalisthenicsExerciseCard(
                        exercise = exercise
                    )
                }
            }
        }
    }
}

@Composable
fun WeightsExerciseCard(
    exercise: ExerciseUiState,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
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
        WeightsExerciseDetails(exercise = exercise)
    }
}

@Composable
fun WeightsExerciseDetails(exercise: ExerciseUiState) {
    if (exercise.muscleGroup != "" || exercise.equipment != "") {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (exercise.muscleGroup != "") {
                ExerciseDetail(
                    exerciseInfo = exercise.muscleGroup,
                    iconId = R.drawable.info_48px,
                    iconDescription = R.string.muscle_icon
                )
            }
            if (exercise.equipment != "") {
                ExerciseDetail(
                    exerciseInfo = exercise.equipment,
                    iconId = R.drawable.exercise_filled_48px,
                    iconDescription = R.string.equipment_icon
                )
            }
        }
    } else {
        ExerciseDetail(
            exerciseInfo = stringResource(id = R.string.weights),
            iconId = R.drawable.exercise_filled_48px,
            iconDescription = R.string.equipment_icon,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CardioExerciseCard(
    exercise: ExerciseUiState,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
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
        CardioExerciseDetails()
    }
}

@Composable
fun CardioExerciseDetails() {
    ExerciseDetail(
        exerciseInfo = stringResource(id = R.string.cardio),
        iconId = R.drawable.cardio_48dp,
        iconDescription = R.string.cardio_icon,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CalisthenicsExerciseCard(
    exercise: ExerciseUiState,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
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
        CalisthenicsExerciseDetails(exercise)
    }
}

@Composable
fun CalisthenicsExerciseDetails(exercise: ExerciseUiState) {
    if (exercise.muscleGroup != "") {
        ExerciseDetail(
            exerciseInfo = exercise.muscleGroup,
            iconId = R.drawable.info_48px,
            iconDescription = R.string.muscle_icon,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        ExerciseDetail(
            exerciseInfo = stringResource(id = R.string.calisthenics),
            iconId = R.drawable.info_48px,
            iconDescription = R.string.calisthenics_icon,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ExerciseDetail(
    exerciseInfo: String,
    iconId: Int,
    @StringRes iconDescription: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = iconDescription),
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
        ExerciseCard(
            exercise = ExerciseUiState(0, "Curls"),
            navigationFunction = { _, _ -> (Unit) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeightsExerciseCardPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExerciseCard(
            exercise = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells"),
            navigationFunction = { _, _ -> (Unit) }
        )
    }
}
