package com.askein.gymtracker.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.AppViewModelProvider
import com.askein.gymtracker.ui.DropdownBox
import com.askein.gymtracker.ui.customCardElevation
import com.askein.gymtracker.ui.navigation.NavigationRoute
import com.askein.gymtracker.ui.navigation.NavigationRoutes
import com.askein.gymtracker.ui.navigation.TopBar
import com.askein.gymtracker.ui.theme.GymTrackerTheme

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
    val uiState = LocalUserPreferences.current
    UserPreferencesScreen(
        uiState = uiState,
        navController = navController,
        defaultDistanceUnitOnChange = { distanceUnit ->
            viewModel.updateDefaultDistanceUnit(distanceUnit)
        },
        defaultWeightUnitOnChange = { weightUnit ->
            viewModel.updateDefaultWeightUnit(weightUnit)
        },
        displayHighestWeightOnChange = { viewModel.updateDisplayHighestWeight(it) },
        displayShortestTimeOnChange = { viewModel.updateDisplayShortestTime(it) },
        modifier = modifier
    )
}

@Composable
fun UserPreferencesScreen(
    uiState: UserPreferencesUiState,
    navController: NavHostController,
    defaultDistanceUnitOnChange: (Int) -> Unit,
    defaultWeightUnitOnChange: (Int) -> Unit,
    displayHighestWeightOnChange: (Boolean) -> Unit,
    displayShortestTimeOnChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                text = stringResource(id = R.string.user_preferences),
                navController = navController,
                settingsScreen = true
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.distances),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    val distancesContentDescription = stringResource(id = R.string.distance_choices)
                    DropdownBox(
                        options = DistanceUnits.values().associateWith { it.displayName },
                        onChange = { value ->
                            defaultDistanceUnitOnChange(value.shortForm)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .semantics { contentDescription = distancesContentDescription },
                        selected = uiState.defaultDistanceUnit
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.weight),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    val weightsContentDescription = stringResource(id = R.string.weight_choices)
                    DropdownBox(
                        options = WeightUnits.values().associateWith { it.displayName },
                        onChange = { value ->
                            defaultWeightUnitOnChange(value.shortForm)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .semantics { contentDescription = weightsContentDescription },
                        selected = uiState.defaultWeightUnit
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.highest_single_weight_rep),
                        textAlign = TextAlign.Right,
                        fontWeight = if (uiState.displayHighestWeight) null else FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    val weightToggleContentDescription = stringResource(id = R.string.weight_display_toggle)
                    Switch(
                        checked = uiState.displayHighestWeight,
                        onCheckedChange = displayHighestWeightOnChange,
                        modifier = Modifier
                            .semantics {
                                contentDescription = weightToggleContentDescription
                            }
                            .padding(horizontal = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.most_weight_per_set),
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
                        text = stringResource(id = R.string.longest_time),
                        textAlign = TextAlign.Right,
                        fontWeight = if (uiState.displayShortestTime) null else FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    val timeToggleContentDescription = stringResource(id = R.string.time_display_toggle)
                    Switch(
                        checked = uiState.displayShortestTime,
                        onCheckedChange = displayShortestTimeOnChange,
                        modifier = Modifier
                            .semantics { contentDescription = timeToggleContentDescription }
                            .padding(horizontal = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.shortest_time),
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
