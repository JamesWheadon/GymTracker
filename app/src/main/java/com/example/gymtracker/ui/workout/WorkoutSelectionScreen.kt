package com.example.gymtracker.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.customCardElevation

@Composable
fun LiveRecordChooseWorkoutsScreen(
    workoutNavigationFunction: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val workoutListUiState by viewModel.workoutListUiState.collectAsState()
    Box {
        LiveRecordChooseWorkoutsScreen(
            workoutListUiState = workoutListUiState,
            workoutNavigationFunction = workoutNavigationFunction,
            modifier = modifier,
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-8).dp, 8.dp),
            onClick = { onDismiss() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
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
        Text(text = "Select Workout to record")
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
