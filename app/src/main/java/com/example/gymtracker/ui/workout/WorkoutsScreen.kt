package com.example.gymtracker.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.navigation.HomeNavigationInformation
import com.example.gymtracker.ui.navigation.HomeScreenCardWrapper
import com.example.gymtracker.ui.navigation.NavigationArguments
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.example.gymtracker.ui.workout.create.CreateWorkoutForm

object WorkoutsRoute : NavigationRoute {
    override val route = NavigationArguments.WORKOUTS_SCREEN.route
}

@Composable
fun WorkoutsScreen(
    navController: NavHostController,
    workoutNavigationFunction: (Int) -> Unit,
    homeNavigationOptions: Map<HomeNavigationInformation, Boolean>,
    modifier: Modifier = Modifier,
    viewModel: WorkoutScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val workoutListUiState by viewModel.workoutListUiState.collectAsState()
    WorkoutsScreen(
        workoutListUiState = workoutListUiState,
        createWorkout = { workout -> viewModel.saveWorkout(workout) },
        navController = navController,
        workoutNavigationFunction = workoutNavigationFunction,
        homeNavigationOptions = homeNavigationOptions,
        modifier = modifier,
    )
}

@Composable
fun WorkoutsScreen(
    workoutListUiState: WorkoutListUiState,
    createWorkout: (Workout) -> Unit,
    navController: NavHostController,
    workoutNavigationFunction: (Int) -> Unit,
    homeNavigationOptions: Map<HomeNavigationInformation, Boolean>,
    modifier: Modifier = Modifier
) {
    var showCreate by remember { mutableStateOf(false) }
    HomeScreenCardWrapper(
        title = "My Workouts",
        navController = navController,
        homeNavigationOptions = homeNavigationOptions,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreate = true },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.Black,
                    contentDescription = "Add Workout"
                )
            }
        }
    ) {
        WorkoutsScreen(
            workoutListUiState = workoutListUiState,
            workoutNavigationFunction = workoutNavigationFunction,
            modifier = modifier
        )
    }
    if (showCreate) {
        Dialog(
            onDismissRequest = { showCreate = false }
        ) {
            CreateWorkoutForm(
                saveFunction = createWorkout,
                onDismiss = { showCreate = false }
            )
        }
    }
}

@Composable
private fun WorkoutsScreen(
    workoutListUiState: WorkoutListUiState,
    workoutNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(workoutListUiState.workoutList) { workout ->
            WorkoutCard(
                workout = workout,
                navigationFunction = workoutNavigationFunction,
                modifier = Modifier.padding(16.dp, 0.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(0.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutCard(
    workout: WorkoutUiState,
    navigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 8.dp,
        pressedElevation = 2.dp,
        focusedElevation = 4.dp
    )
    Card(
        modifier = modifier,
        elevation = customCardElevation,
        onClick = { navigationFunction(workout.workoutId) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = workout.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutsScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        WorkoutsScreen(
            workoutListUiState = WorkoutListUiState(
                listOf(
                    WorkoutUiState(0, "Arms"),
                    WorkoutUiState(1, "Shoulders"),
                    WorkoutUiState(2, "Back")
                )
            ),
            workoutNavigationFunction = { }
        )
    }
}
