package com.example.gymtracker.data.exerciseHistory.weights

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.gymtracker.data.database.ExerciseWorkoutDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

private const val FIRST_EXERCISE_ID = 1
private const val SECOND_EXERCISE_ID = 2
private const val FIRST_HISTORY_ID = 1
private const val SECOND_HISTORY_ID = 2
private const val THIRD_HISTORY_ID = 3
private const val NEW_WEIGHT = 20.0

class WeightsExerciseHistoryDaoTest {

    private lateinit var weightsExerciseHistoryDao: WeightsExerciseHistoryDao
    private lateinit var exerciseWorkoutDatabase: ExerciseWorkoutDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        exerciseWorkoutDatabase = Room.inMemoryDatabaseBuilder(context, ExerciseWorkoutDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        weightsExerciseHistoryDao = exerciseWorkoutDatabase.weightsExerciseHistoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        exerciseWorkoutDatabase.close()
    }

    @Test
    fun daoInsertAndDaoSelect_insertsExerciseHistoryIntoDBAndRetrievesById() = runBlocking {
        val exerciseHistory = createExerciseHistory(FIRST_HISTORY_ID, FIRST_EXERCISE_ID)
        weightsExerciseHistoryDao.insert(exerciseHistory)

        val savedExerciseHistory = weightsExerciseHistoryDao.getHistory(exerciseHistory.id).first()

        Assert.assertEquals(exerciseHistory, savedExerciseHistory)
    }

    @Test
    fun daoSelectAllByExercise_RetrieveFullHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        val savedExerciseHistory = weightsExerciseHistoryDao.getFullExerciseHistory(
            FIRST_EXERCISE_ID
        ).first()

        assertThat(savedExerciseHistory.size, equalTo(2))
    }

    @Test
    fun daoSelectMostRecentExercise_RetrieveLatestHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        val savedExerciseHistory = weightsExerciseHistoryDao.getLatestExerciseHistory(
            FIRST_EXERCISE_ID
        ).first()

        assertThat(savedExerciseHistory, equalTo(createExerciseHistory(SECOND_HISTORY_ID, FIRST_EXERCISE_ID)))
    }

    @Test
    fun daoSelectExerciseInPastWeek_RetrieveRecentHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        val savedExerciseHistory = weightsExerciseHistoryDao.getRecentExerciseHistory(
            FIRST_EXERCISE_ID, 7).first()

        assertThat(savedExerciseHistory.size, equalTo(1))
    }

    @Test
    fun daoSelectExerciseInPastMonth_RetrieveRecentHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()


        val savedExerciseHistory = weightsExerciseHistoryDao.getRecentExerciseHistory(
            FIRST_EXERCISE_ID, 31).first()

        assertThat(savedExerciseHistory.size, equalTo(2))
    }

    @Test
    fun daoDelete_DeleteHistoryFromDB() = runBlocking {
        val exerciseHistory = createExerciseHistory(FIRST_HISTORY_ID, FIRST_EXERCISE_ID)
        weightsExerciseHistoryDao.insert(exerciseHistory)

        weightsExerciseHistoryDao.delete(exerciseHistory)

        val savedHistory = weightsExerciseHistoryDao.getHistory(FIRST_HISTORY_ID).first()
        assertThat(savedHistory, equalTo(null))
    }

    @Test
    fun daoUpdate_UpdateHistoryInDB() = runBlocking {
        val exerciseHistory = createExerciseHistory(FIRST_HISTORY_ID, FIRST_EXERCISE_ID)
        weightsExerciseHistoryDao.insert(exerciseHistory)

        val updateExerciseHistory = createExerciseHistory(FIRST_HISTORY_ID, FIRST_EXERCISE_ID, weight = NEW_WEIGHT)
        weightsExerciseHistoryDao.update(updateExerciseHistory)

        val savedHistory = weightsExerciseHistoryDao.getHistory(FIRST_HISTORY_ID).first()
        assertThat(savedHistory.weight, equalTo(NEW_WEIGHT))
    }

    @Test
    fun daoDelete_DeleteAllHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        weightsExerciseHistoryDao.deleteAllForExercise(FIRST_EXERCISE_ID)

        val savedHistory = weightsExerciseHistoryDao.getFullExerciseHistory(FIRST_EXERCISE_ID).first()
        assertThat(savedHistory.size, equalTo(0))
    }

    private fun createExerciseHistory(id: Int, exerciseId: Int, time: LocalDate = LocalDate.now(), weight: Double = 10.0) = WeightsExerciseHistory(id, exerciseId, weight, 10, 10, time)

    private suspend fun addMultipleHistoryToDB() {
        weightsExerciseHistoryDao.insert(createExerciseHistory(FIRST_HISTORY_ID, FIRST_EXERCISE_ID, LocalDate.now().minusDays(8)))
        weightsExerciseHistoryDao.insert(createExerciseHistory(SECOND_HISTORY_ID, FIRST_EXERCISE_ID))
        weightsExerciseHistoryDao.insert(createExerciseHistory(THIRD_HISTORY_ID, SECOND_EXERCISE_ID))
    }
}
