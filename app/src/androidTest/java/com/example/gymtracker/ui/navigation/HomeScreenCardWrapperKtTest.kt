package com.example.gymtracker.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class HomeScreenCardWrapperKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Mock
    private lateinit var navController: NavHostController

    private val title = rule.onNode(hasText("Test Title"))
    private val content = rule.onNode(hasText("Test Content"))
    private val workoutsButton = rule.onNode(hasText("Workouts"))
    private val exercisesButton = rule.onNode(hasText("Exercises"))
    private val floatingButton = rule.onNode(hasText("Floating Button"))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun rendersHomeScreenCardWrapper() {
        rule.setContent {
            HomeScreenCardWrapper(
                title = "Test Title",
                navController = navController,
                homeNavigationOptions = mapOf(
                    HomeNavigationInformation("Workouts") { } to true,
                    HomeNavigationInformation("Exercises") { } to false
                ),
                floatingActionButton = {
                    Button(onClick = { }) {
                        Text(text = "Floating Button")
                    }
                }
            ) {
                Text(text = "Test Content")
            }
        }

        title.assertExists()
        content.assertExists()
        workoutsButton.assertExists()
        workoutsButton.assertIsEnabled()
        exercisesButton.assertExists()
        exercisesButton.assertIsNotEnabled()
        floatingButton.assertExists()
    }

    @Test
    fun clickingHomeNavigationButtonCallsNavigationFunction() {
        var workouts = false
        var exercises = false

        rule.setContent {
            HomeScreenCardWrapper(
                title = "Test Title",
                navController = navController,
                homeNavigationOptions = mapOf(
                    HomeNavigationInformation("Workouts") { workouts = true } to true,
                    HomeNavigationInformation("Exercises") { exercises = true } to false
                ),
                floatingActionButton = {
                    Button(onClick = { }) {
                        Text(text = "Floating Button")
                    }
                }
            ) {
                Text(text = "Test Content")
            }
        }

        workoutsButton.performClick()
        exercisesButton.performClick()

        assertThat(workouts, equalTo(true))
        assertThat(exercises, equalTo(false))
    }

    @Test
    fun clickingFloatingButtonCallsFunction() {
        var floatingAction = false

        rule.setContent {
            HomeScreenCardWrapper(
                title = "Test Title",
                navController = navController,
                homeNavigationOptions = mapOf(),
                floatingActionButton = {
                    Button(onClick = { floatingAction = true }) {
                        Text(text = "Floating Button")
                    }
                }
            ) {
                Text(text = "Test Content")
            }
        }

        floatingButton.performClick()

        assertThat(floatingAction, equalTo(true))
    }
}
