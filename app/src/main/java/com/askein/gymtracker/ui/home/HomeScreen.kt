package com.askein.gymtracker.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.navigation.NavigationRoute
import com.askein.gymtracker.ui.navigation.NavigationRoutes
import com.askein.gymtracker.ui.navigation.TopBar
import java.time.LocalDate

object HomeScreenRoute : NavigationRoute {
    override val route = NavigationRoutes.HOME_SCREEN.baseRoute
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    exerciseNavigationFunction: (Int, LocalDate?) -> Unit,
    workoutNavigationFunction: (Int, LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    var homeData by remember { mutableStateOf(HomeData(
        workoutNavigationFunction = workoutNavigationFunction,
        exerciseNavigationFunction = exerciseNavigationFunction
    )) }
    val homeScreens = mapOf(
        R.string.workouts to WorkoutsScreenHomeView(),
        R.string.exercises to ExercisesScreenHomeView(),
        R.string.history to HistoryScreenHomeView()
    )
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                text = stringResource(id = R.string.home),
                navController = navController,
                homeScreen = true
            )
        },
        floatingActionButton = {
            homeScreens[homeData.selectedScreen]!!.FloatingActionButton(
                homeData = homeData,
                homeDataOnChange = { newValue -> homeData = newValue }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                for (option in homeScreens.keys) {
                    Button(
                        onClick = { homeData = homeData.copy(selectedScreen = option) },
                        enabled = homeData.selectedScreen != option
                    ) {
                        Text(text = stringResource(id = option))
                    }
                }
            }
            homeScreens[homeData.selectedScreen]!!.ScreenContent(
                homeData = homeData,
                homeDataOnChange = { newValue -> homeData = newValue }
            )
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

data class HomeData(
    var selectedScreen: Int = R.string.workouts,
    var showCreateWorkout: Boolean = false,
    var showCreateExercise: Boolean = false,
    val workoutNavigationFunction: (Int, LocalDate?) -> Unit,
    val exerciseNavigationFunction: (Int, LocalDate?) -> Unit
)
