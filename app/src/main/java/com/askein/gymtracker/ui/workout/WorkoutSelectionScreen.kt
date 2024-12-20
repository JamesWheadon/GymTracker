package com.askein.gymtracker.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.navigation.NavigationRoute
import com.askein.gymtracker.ui.navigation.NavigationRoutes.WORKOUT_SELECTION_SCREEN
import com.askein.gymtracker.ui.navigation.TopBar
import java.time.LocalDate

object WorkoutSelectionScreenRoute : NavigationRoute {
    override val route = WORKOUT_SELECTION_SCREEN.baseRoute
}

@Composable
fun LiveRecordChooseWorkoutsScreen(
    navController: NavHostController,
    workoutNavigationFunction: (Int, LocalDate?) -> Unit,
    viewModel: WorkoutScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val workoutListUiState by viewModel.workoutListUiState.collectAsState()
    Scaffold(
        topBar = {
            TopBar(
                text = stringResource(id = R.string.select_record_workout),
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
    workoutNavigationFunction: (Int, LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp
        ),
        modifier = modifier.padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        val columnContentDescription = stringResource(id = R.string.workout_column)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = columnContentDescription },
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(workoutListUiState.workoutList.sortedBy { it.name }) { workout ->
                WorkoutCard(
                    workout = workout,
                    navigationFunction = workoutNavigationFunction
                )
            }
        }
    }
}
