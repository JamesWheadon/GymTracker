package com.example.gymtracker.ui.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
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
    newExerciseFunction: () -> Unit,
    exerciseNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExercisesScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val exerciseListUiState by viewModel.exerciseListUiState.collectAsState()
    ExerciseScreen(
        newExerciseFunction = newExerciseFunction,
        exerciseNavigationFunction = exerciseNavigationFunction,
        exerciseListUiState = exerciseListUiState,
        modifier
    )
}

@Composable
fun ExerciseScreen(
    newExerciseFunction: () -> Unit,
    exerciseNavigationFunction: (Int) -> Unit,
    exerciseListUiState: ExerciseListUiState,
    modifier: Modifier = Modifier
) {
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 16.dp
    )
    Card(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 10.dp, horizontal = 10.dp),
        elevation = customCardElevation
    ) {
        Box {
            Column {
                Text(
                    text = "My Exercises",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(CenterHorizontally)
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                )
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(exerciseListUiState.exerciseList) { exercise ->
                        ExerciseCard(
                            exercise = exercise,
                            navigationFunction = exerciseNavigationFunction
                        )
                    }
                }
            }
            Button(
                onClick = { newExerciseFunction() },
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomEnd)
                    .padding(20.dp, 20.dp, 20.dp, 20.dp)
                    .background(
                        MaterialTheme.colorScheme.primary
                            .copy(alpha = 0.75f)
                            .compositeOver(Color.White),
                        shape = CircleShape
                    ),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray.copy(alpha = 0.85f),
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 16.dp
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = "create new exercise")
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    navigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 8.dp,
        pressedElevation = 2.dp,
        focusedElevation = 4.dp
    )
    Button(onClick = { navigationFunction(exercise.id) }) {
        Card(
            modifier = modifier,
            elevation = customCardElevation
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
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ExerciseDetail(
                        exerciseInfo = exercise.muscleGroup,
                        iconId = R.drawable.info_48px,
                        iconDescription = "exercise icon"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExerciseDetail(
                        exerciseInfo = exercise.equipment,
                        iconId = R.drawable.exercise_filled_48px,
                        iconDescription = "exercise icon"
                    )
                }
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
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = exerciseInfo,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExerciseScreen(
            {},
            {},
            exerciseListUiState = ExerciseListUiState(
                listOf(
                    Exercise(0, "Curls", "Biceps", "Dumbbells"),
                    Exercise(1, "Dips", "Triceps", "Dumbbells And Bars"),
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
