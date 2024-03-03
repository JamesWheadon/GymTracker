package com.example.gymtracker.ui.navigation

import androidx.navigation.NavHostController
import com.example.gymtracker.ui.exercise.ExercisesRoute
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mockito.mock

class HomeNavigationInformationKtTest {

    private val navController: NavHostController = mock()

    @Test
    fun getsHomeNavigationRoutes() {
        val navigationMap = getHomeNavigationOptionsForRoute(
            homeRoute = ExercisesRoute,
            navController = navController
        )

        val navigationTitles = navigationMap.keys
        assertThat(navigationMap.size, equalTo(3))
        assertThat(navigationTitles.map { it.title }, hasItems("Exercises", "Workouts", "History"))
        assertThat(navigationMap[navigationTitles.first { it.title == "Exercises" }], equalTo(false))
        assertThat(navigationMap[navigationTitles.first { it.title == "Workouts" }], equalTo(true))
        assertThat(navigationMap[navigationTitles.first { it.title == "History" }], equalTo(true))
    }
}
