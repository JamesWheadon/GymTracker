package com.example.gymtracker.data.user

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.gymtracker.enums.DistanceUnits
import com.example.gymtracker.enums.WeightUnits
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class OfflineUserPreferencesRepositoryTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val repository: OfflineUserPreferencesRepository = OfflineUserPreferencesRepository(testContext)

    @Test
    fun userPreferencesRepository_testSaveDefaultWeightUnit() = runBlocking {
        repository.saveDefaultWeightUnit(WeightUnits.POUNDS.shortForm)
        assertThat(repository.isDefaultWeightUnit.first(), equalTo(WeightUnits.POUNDS.shortForm))
    }

    @Test
    fun userPreferencesRepository_testSaveDefaultDistanceUnit() = runBlocking {
        repository.saveDefaultDistanceUnit(DistanceUnits.MILES.shortForm)
        assertThat(repository.isDefaultDistanceUnit.first(), equalTo(DistanceUnits.MILES.shortForm))
    }

    @Test
    fun userPreferencesRepository_testSaveDisplayHighestWeight() = runBlocking {
        repository.saveDisplayHighestWeight(false)
        assertThat(repository.isDisplayHighestWeight.first(), equalTo(false))
    }

    @Test
    fun userPreferencesRepository_testSaveDisplayShortestTime() = runBlocking {
        repository.saveDisplayShortestTime(false)
        assertThat(repository.isDisplayShortestTime.first(), equalTo(false))
    }
}
