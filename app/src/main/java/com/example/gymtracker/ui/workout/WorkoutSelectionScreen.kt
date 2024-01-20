package com.example.gymtracker.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.navigation.HomeScreenCardWrapper
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.navigation.NavigationRoutes.WORKOUT_SELECTION_SCREEN
import com.example.gymtracker.ui.navigation.TopBar
import com.example.gymtracker.ui.workout.details.WorkoutDetailsScreen

object WorkoutSelectionScreenRoute : NavigationRoute {
    override val route = WORKOUT_SELECTION_SCREEN.baseRoute
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveRecordChooseWorkoutsScreen(
    navController: NavHostController,
    workoutNavigationFunction: (Int) -> Unit,
    viewModel: WorkoutScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val workoutListUiState by viewModel.workoutListUiState.collectAsState()
    Scaffold(
        topBar = {
            TopBar(
                text = "Select Workout to record",
                navController = navController
            )
        }
    ) { innerPadding ->
        LiveRecordChooseWorkoutsScreen(
            workoutListUiState = workoutListUiState,
            workoutNavigationFunction = workoutNavigationFunction,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun LiveRecordChooseWorkoutsScreen(
    workoutListUiState: WorkoutListUiState,
    workoutNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = customCardElevation()
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
}
