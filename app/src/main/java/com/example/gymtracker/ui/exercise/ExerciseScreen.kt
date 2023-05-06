package com.example.gymtracker.ui.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.R
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.theme.GymTrackerTheme

@Composable
fun ExerciseScreen(
    modifier: Modifier = Modifier,
    viewModel: ExerciseViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    ExerciseScreen(
        uiState = uiState,
        modifier
    )
}

@Composable
fun ExerciseScreen(
    uiState: ExerciseScreenUiState,
    modifier: Modifier = Modifier
) {
    Card() {
        Column() {
            Text(
                text = "My Exercises",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            )
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.exerciseList) { exercise ->
                    ExerciseCard(exercise = exercise)
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
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
                modifier = Modifier.fillMaxWidth(0.6f)
            )
            Column(
            ) {
                ExerciseDetail(
                    exerciseInfo = exercise.muscleGroup,
                    iconId = R.drawable.info_48px,
                    iconDescription = "exercise icon"
                )
                ExerciseDetail(
                    exerciseInfo = exercise.equipment,
                    iconId = R.drawable.exercise_filled_48px,
                    iconDescription = "exercise icon"
                )
            }
        }
    }
}

@Composable
fun ExerciseDetail(
    exerciseInfo: String,
    iconId: Int,
    iconDescription: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = iconDescription,
            modifier = Modifier.size(20.dp)
        )
        Text(text = exerciseInfo)
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    GymTrackerTheme {
        ExerciseScreen(
            uiState = ExerciseScreenUiState(
                exerciseList = listOf(
                    Exercise(0, "Curls", "Biceps", "Dumbbells"),
                    Exercise(1, "Dips", "Triceps", "Dumbbells"),
                    Exercise(
                        2,
                        "Testing what happens if someone decides to have a ridiculously long exercise name",
                        "Lats",
                        "Dumbbells"
                    ),
                )
            )
        )
    }
}