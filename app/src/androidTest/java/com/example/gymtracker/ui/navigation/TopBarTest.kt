package com.example.gymtracker.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TopBarTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val backButton = rule.onNode(hasContentDescription("Back Button"))
    private val homeButton = rule.onNode(hasContentDescription("Home Button"))
    private val editButton = rule.onNode(hasContentDescription("Edit feature"))
    private val deleteButton = rule.onNode(hasContentDescription("Delete feature"))

    @Mock
    private lateinit var navController: NavHostController
    @Mock
    private lateinit var navGraph: NavGraph

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(navController.previousBackStackEntry).thenReturn(null)
        `when`(navController.currentDestination).thenReturn(null)
    }

    @Test
    fun buttonsDoNotExistWithNoFunctionsAndBackStack() {
        rule.setContent {
            TopBar(
                text = "text test", navController = navController
            )
        }

        backButton.assertDoesNotExist()
        homeButton.assertDoesNotExist()
        editButton.assertDoesNotExist()
        deleteButton.assertDoesNotExist()
    }

    @Test
    fun clickingBackButtonExecutesNavigateBack() {
        `when`(navController.previousBackStackEntry).thenReturn(NavBackStackEntry.create(null, NavDestination("")))

        rule.setContent {
            TopBar(
                text = "text test", navController = navController
            )
        }

        backButton.assertExists()
        editButton.assertDoesNotExist()
        deleteButton.assertDoesNotExist()

        backButton.performClick()

        verify(navController).popBackStack()
    }

    @Test
    fun clickingHomeButtonExecutesNavigateToHome() {
        `when`(navController.currentDestination).thenReturn(NavDestination(NavigationRoutes.EXERCISES_SCREEN.baseRoute))
        `when`(navController.graph).thenReturn(navGraph)

        rule.setContent {
            TopBar(
                text = "text test", navController = navController
            )
        }

        backButton.assertDoesNotExist()
        homeButton.assertExists()
        editButton.assertDoesNotExist()
        deleteButton.assertDoesNotExist()

        homeButton.performClick()

        verify(navController).navigate(anyInt())
    }

    @Test
    fun clickingEditButtonExecutesEditFunction() {
        var clicked = false
        rule.setContent {
            TopBar(
                text = "text test",
                navController = navController,
                editFunction = { clicked = true })
        }

        backButton.assertDoesNotExist()
        homeButton.assertDoesNotExist()
        editButton.assertExists()
        deleteButton.assertDoesNotExist()

        editButton.performClick()

        assertThat(clicked, equalTo(true))
    }

    @Test
    fun clickingDeleteButtonExecutesDeleteFunction() {
        var clicked = false
        rule.setContent {
            TopBar(
                text = "text test",
                navController = navController
            ) { clicked = true }
        }

        backButton.assertDoesNotExist()
        homeButton.assertDoesNotExist()
        editButton.assertDoesNotExist()
        deleteButton.assertExists()

        deleteButton.performClick()

        assertThat(clicked, equalTo(true))
    }
}
