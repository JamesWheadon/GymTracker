package com.example.gymtracker.ui.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.R
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.exercise.create.CreateExerciseScreen
import com.example.gymtracker.ui.navigation.TopBar
import com.example.gymtracker.ui.theme.GymTrackerTheme

@Composable
fun ExerciseScreen(
    exerciseNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExercisesScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val exerciseListUiState by viewModel.exerciseListUiState.collectAsState()
    ExerciseScreen(
        exerciseNavigationFunction = exerciseNavigationFunction,
        exerciseListUiState = exerciseListUiState,
        modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(
    exerciseNavigationFunction: (Int) -> Unit,
    exerciseListUiState: ExerciseListUiState,
    modifier: Modifier = Modifier
) {
    var showCreate by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                text = "My Exercises",
                backEnabled = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreate = true },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.Black,
                    contentDescription = "Add Exercise"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxWidth(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(exerciseListUiState.exerciseList) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    navigationFunction = exerciseNavigationFunction
                )
            }
        }
    }
    if (showCreate) {
        Dialog(
            onDismissRequest = { showCreate = false }
        ) {
            CreateExerciseScreen(
                onDismiss = { showCreate = false }
            )
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
    Button(
        shape = RectangleShape,
        onClick = { navigationFunction(exercise.id) }
    ) {
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
                    modifier = Modifier.fillMaxWidth(0.55f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
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
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        ExerciseScreen(
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
