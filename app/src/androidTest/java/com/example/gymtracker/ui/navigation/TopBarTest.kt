package com.example.gymtracker.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import com.example.gymtracker.ui.user.UserPreferencesRoute
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
import org.mockito.kotlin.argThat
import org.mockito.kotlin.isNull

class TopBarTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val backButton = rule.onNode(hasContentDescription("back button"))
    private val homeButton = rule.onNode(hasContentDescription("home button"))
    private val editButton = rule.onNode(hasContentDescription("edit feature"))
    private val deleteButton = rule.onNode(hasContentDescription("delete feature"))
    private val settingsButton = rule.onNode(hasContentDescription("user settings"))

    @Mock
    private lateinit var navController: NavHostController

    @Mock
    private lateinit var navGraph: NavGraph

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        `when`(navController.previousBackStackEntry).thenReturn(null)
        `when`(navController.currentDestination).thenReturn(null)
    }

    @Test
    fun buttonsDoNotExistWithNoFunctionsAndBackStackExceptUserSettings() {
        rule.setContent {
            TopBar(
                text = "text test", navController = navController
            )
        }

        backButton.assertDoesNotExist()
        homeButton.assertDoesNotExist()
        editButton.assertDoesNotExist()
        deleteButton.assertDoesNotExist()
        settingsButton.assertExists()
    }

    @Test
    fun clickingBackButtonExecutesNavigateBack() {
        `when`(navController.previousBackStackEntry).thenReturn(
            NavBackStackEntry.create(
                null,
                NavDestination("")
            )
        )

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
                navController = navController,
                deleteFunction = { clicked = true }
            )
        }

        backButton.assertDoesNotExist()
        homeButton.assertDoesNotExist()
        editButton.assertDoesNotExist()
        deleteButton.assertExists()

        deleteButton.performClick()

        assertThat(clicked, equalTo(true))
    }

    @Test
    fun clickingProfileButtonNavigatesToUserPreferences() {
        rule.setContent {
            TopBar(
                text = "text test",
                navController = navController
            )
        }

        settingsButton.performClick()

        verify(navController).navigate(argThat<NavDeepLinkRequest> { aBar: NavDeepLinkRequest ->
            aBar.uri?.encodedPath?.contains(UserPreferencesRoute.route) ?: false
        }, isNull(), isNull())
    }

    @Test
    fun doesNotRenderProfileIconOnSettingsPage() {
        rule.setContent {
            TopBar(
                text = "text test",
                navController = navController,
                settingsScreen = true
            )
        }

        settingsButton.assertDoesNotExist()
    }
}
