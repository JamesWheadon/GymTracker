package com.example.gymtracker.ui.user

import app.cash.turbine.test
import com.example.gymtracker.data.user.UserPreferencesRepository
import com.example.gymtracker.enums.DistanceUnits
import com.example.gymtracker.enums.WeightUnits
import com.example.gymtracker.fake.FakeUserPreferencesRepository
import com.example.gymtracker.rules.TestCoroutineRule
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserPreferencesViewModelTest {

    private val repository: UserPreferencesRepository = mock()
    private val fakeRepository = FakeUserPreferencesRepository()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun getUserPreferencesFromRepository() = runTest {
        val viewModel = UserPreferencesViewModel(fakeRepository)

        viewModel.uiState.test {
            val uiState = awaitItem()
            assertThat(uiState.defaultDistanceUnit, equalTo(DistanceUnits.METERS))
            assertThat(uiState.defaultWeightUnit, equalTo(WeightUnits.POUNDS))
            assertThat(uiState.displayHighestWeight, equalTo(false))
            assertThat(uiState.displayShortestTime, equalTo(false))
        }
    }

    @Test
    fun updateDefaultDistanceUnitInRepository() = runTest {
        val viewModel = UserPreferencesViewModel(repository)

        viewModel.updateDefaultDistanceUnit(DistanceUnits.MILES)

        verify(repository).saveDefaultDistanceUnit(DistanceUnits.MILES.shortForm)
    }

    @Test
    fun updateDefaultWeightUnitInRepository() = runTest {
        val viewModel = UserPreferencesViewModel(repository)

        viewModel.updateDefaultWeightUnit(WeightUnits.POUNDS)

        verify(repository).saveDefaultWeightUnit(WeightUnits.POUNDS.shortForm)
    }

    @Test
    fun updateDisplayHighestWeightInRepository() = runTest {
        val viewModel = UserPreferencesViewModel(repository)

        viewModel.updateDisplayHighestWeight(false)

        verify(repository).saveDisplayHighestWeight(false)
    }

    @Test
    fun updateDisplayShortestTimeInRepository() = runTest {
        val viewModel = UserPreferencesViewModel(repository)

        viewModel.updateDisplayShortestTime(false)

        verify(repository).saveDisplayShortestTime(false)
    }
}
