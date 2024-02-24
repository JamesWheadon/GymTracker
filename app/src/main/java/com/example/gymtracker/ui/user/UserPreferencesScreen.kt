package com.example.gymtracker.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gymtracker.converters.DistanceUnits
import com.example.gymtracker.converters.WeightUnits
import com.example.gymtracker.ui.AppViewModelProvider
import com.example.gymtracker.ui.DropdownBox
import com.example.gymtracker.ui.customCardElevation
import com.example.gymtracker.ui.navigation.NavigationRoute
import com.example.gymtracker.ui.navigation.NavigationRoutes
import com.example.gymtracker.ui.navigation.TopBar
import com.example.gymtracker.ui.theme.GymTrackerTheme

object UserPreferencesRoute : NavigationRoute {
    override val route = NavigationRoutes.USER_PREFERENCES_SCREEN.baseRoute
}

@Composable
fun UserPreferencesScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: UserPreferencesViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    UserPreferencesScreen(
        uiState = uiState,
        navController = navController,
        defaultDistanceUnitOnChange = { distanceUnit -> viewModel.updateDefaultDistanceUnit(DistanceUnits.valueOf(distanceUnit)) },
        defaultWeightUnitOnChange = { weightUnit -> viewModel.updateDefaultWeightUnit(WeightUnits.valueOf(weightUnit)) },
        displayHighestWeightOnChange = { viewModel.updateDisplayHighestWeight(it) },
        displayShortestTimeOnChange = { viewModel.updateDisplayShortestTime(it) },
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPreferencesScreen(
    uiState: UserPreferencesUiState,
    navController: NavHostController,
    defaultDistanceUnitOnChange: (String) -> Unit,
    defaultWeightUnitOnChange: (String) -> Unit,
    displayHighestWeightOnChange: (Boolean) -> Unit,
    displayShortestTimeOnChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                text = "User Settings",
                navController = navController
            )
        }
    ) { innerPadding ->
        Card(
            elevation = customCardElevation(),
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Distances",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    DropdownBox(
                        options = DistanceUnits.values().map { it.displayName },
                        onChange = defaultDistanceUnitOnChange,
                        selected = uiState.defaultDistanceUnit.displayName,
                        modifier = Modifier.weight(1f).semantics { contentDescription = "Distance choices" }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Weight",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    DropdownBox(
                        options = WeightUnits.values().map { it.displayName },
                        onChange = defaultWeightUnitOnChange,
                        selected = uiState.defaultWeightUnit.displayName,
                        modifier = Modifier.weight(1f).semantics { contentDescription = "Weight choices" }
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Highest single weight rep",
                        textAlign = TextAlign.Right,
                        fontWeight = if (uiState.displayHighestWeight) null else FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = uiState.displayHighestWeight,
                        onCheckedChange = displayHighestWeightOnChange,
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Weight display toggle"
                            }
                            .padding(horizontal = 8.dp)
                    )
                    Text(
                        text = "Most weight per set",
                        fontWeight = if (uiState.displayHighestWeight) FontWeight.Bold else null,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Longest Time",
                        textAlign = TextAlign.Right,
                        fontWeight = if (uiState.displayShortestTime) null else FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = uiState.displayShortestTime,
                        onCheckedChange = displayShortestTimeOnChange,
                        modifier = Modifier
                            .semantics { contentDescription = "Time display toggle" }
                            .padding(horizontal = 8.dp)
                    )
                    Text(
                        text = "Shortest Time",
                        fontWeight = if (uiState.displayShortestTime) FontWeight.Bold else null,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserPreferencesScreenPreview() {
    GymTrackerTheme(darkTheme = false) {
        UserPreferencesScreen(
            uiState = UserPreferencesUiState(),
            navController = rememberNavController(),
            defaultDistanceUnitOnChange = {},
            defaultWeightUnitOnChange = {},
            displayHighestWeightOnChange = {},
            displayShortestTimeOnChange = {}
        )
    }
}
