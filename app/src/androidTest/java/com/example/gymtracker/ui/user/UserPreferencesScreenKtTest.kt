package com.example.gymtracker.ui.user

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import com.example.gymtracker.R
import com.example.gymtracker.getResourceString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UserPreferencesScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val uiState = UserPreferencesUiState()

    private val distancesLabel = rule.onNode(hasText("Distances"))
    private val weightsLabel = rule.onNode(hasText("Weight"))
    private val distanceDropdown = rule.onNode(hasContentDescription(getResourceString(R.string.distance_choices)))
    private val weightDropdown = rule.onNode(hasContentDescription(getResourceString(R.string.weight_choices)))
    private val singleRep = rule.onNode(hasText("Highest single weight rep"))
    private val highestSet = rule.onNode(hasText("Most weight per set"))
    private val highestWeightToggle = rule.onNode(hasContentDescription(getResourceString(R.string.weight_display_toggle)))
    private val longestTime = rule.onNode(hasText("Longest Time"))
    private val shortestTime = rule.onNode(hasText("Shortest Time"))
    private val shortestTimeToggle = rule.onNode(hasContentDescription(getResourceString(R.string.time_display_toggle)))

    @Mock
    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun rendersUserPreferencesScreen() {
        rule.setContent {
            UserPreferencesScreen(
                uiState = uiState,
                navController = navController,
                defaultDistanceUnitOnChange = { },
                defaultWeightUnitOnChange = { },
                displayHighestWeightOnChange = { },
                displayShortestTimeOnChange = { }
            )
        }

        distancesLabel.assertExists()
        weightsLabel.assertExists()
        distanceDropdown.assertExists()
        weightDropdown.assertExists()
        singleRep.assertExists()
        highestSet.assertExists()
        highestWeightToggle.assertExists()
        longestTime.assertExists()
        shortestTime.assertExists()
        shortestTimeToggle.assertExists()
    }

    @Test
    fun userPreferencesScreenClickingDistanceDropdownOptionCallsDistanceOnChange() {
        var selected: Int? = null
        rule.setContent {
            UserPreferencesScreen(
                uiState = uiState,
                navController = navController,
                defaultDistanceUnitOnChange = { selected = it },
                defaultWeightUnitOnChange = { },
                displayHighestWeightOnChange = { },
                displayShortestTimeOnChange = { }
            )
        }

        distanceDropdown.performClick()
        rule.onNode(hasText("Meters")).performClick()

        assertThat(selected, equalTo(R.string.meters_short_form))
    }

    @Test
    fun userPreferencesScreenClickingWeightDropdownOptionCallsWeightOnChange() {
        var selected: Int? = null
        rule.setContent {
            UserPreferencesScreen(
                uiState = uiState,
                navController = navController,
                defaultDistanceUnitOnChange = { },
                defaultWeightUnitOnChange = { selected = it },
                displayHighestWeightOnChange = { },
                displayShortestTimeOnChange = { }
            )
        }

        weightDropdown.performClick()
        rule.onNode(hasText("Pounds")).performClick()

        assertThat(selected, equalTo(R.string.pounds_short_form))
    }

    @Test
    fun userPreferencesScreenClickingWeightToggleCallsHighestWeightOnChange() {
        var selected: Boolean? = null
        rule.setContent {
            UserPreferencesScreen(
                uiState = uiState,
                navController = navController,
                defaultDistanceUnitOnChange = { },
                defaultWeightUnitOnChange = { },
                displayHighestWeightOnChange = { selected = it },
                displayShortestTimeOnChange = { }
            )
        }

        highestWeightToggle.performClick()

        assertThat(selected, equalTo(!uiState.displayHighestWeight))
    }

    @Test
    fun userPreferencesScreenClickingTimeToggleCallsTimeOnChange() {
        var selected: Boolean? = null
        rule.setContent {
            UserPreferencesScreen(
                uiState = uiState,
                navController = navController,
                defaultDistanceUnitOnChange = { },
                defaultWeightUnitOnChange = { },
                displayHighestWeightOnChange = { },
                displayShortestTimeOnChange = { selected = it }
            )
        }

        shortestTimeToggle.performClick()

        assertThat(selected, equalTo(!uiState.displayShortestTime))
    }
}
