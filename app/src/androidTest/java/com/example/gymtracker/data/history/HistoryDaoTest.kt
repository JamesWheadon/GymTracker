package com.example.gymtracker.data.history

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.gymtracker.data.database.HistoryDatabase
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

class HistoryDaoTest {

    private lateinit var historyDao: HistoryDao
    private lateinit var historyDatabase: HistoryDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        historyDatabase = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        historyDao = historyDatabase.historyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        historyDatabase.close()
    }

    @Test
    fun daoInsertAndDaoSelect_insertsExerciseHistoryIntoDBAndRetrievesById() = runBlocking {
        val exerciseHistory = getExerciseHistory(FIRST_HISTORY_ID, FIRST_EXERCISE_ID)
        historyDao.insert(exerciseHistory)

        val savedExerciseHistory = historyDao.getHistory(exerciseHistory.id).first()

        Assert.assertEquals(exerciseHistory, savedExerciseHistory)
    }

    @Test
    fun daoSelectAllByExercise_RetrieveFullHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        val savedExerciseHistory = historyDao.getFullExerciseHistory(FIRST_EXERCISE_ID).first()

        assertThat(savedExerciseHistory.size, equalTo(2))
    }

    @Test
    fun daoSelectMostRecentExercise_RetrieveLatestHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        val savedExerciseHistory = historyDao.getLatestExerciseHistory(FIRST_EXERCISE_ID).first()

        assertThat(savedExerciseHistory, equalTo(getExerciseHistory(SECOND_HISTORY_ID, FIRST_EXERCISE_ID)))
    }

    @Test
    fun daoSelectExerciseInPastWeek_RetrieveRecentHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        val savedExerciseHistory = historyDao.getRecentExerciseHistory(FIRST_EXERCISE_ID, 7).first()

        assertThat(savedExerciseHistory.size, equalTo(1))
    }

    @Test
    fun daoSelectExerciseInPastMonth_RetrieveRecentHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()


        val savedExerciseHistory = historyDao.getRecentExerciseHistory(FIRST_EXERCISE_ID, 31).first()

        assertThat(savedExerciseHistory.size, equalTo(2))
    }

    @Test
    fun daoDelete_DeleteHistoryFromDB() = runBlocking {
        val exerciseHistory = getExerciseHistory(FIRST_HISTORY_ID, FIRST_EXERCISE_ID)
        historyDao.insert(exerciseHistory)

        historyDao.delete(exerciseHistory)

        val savedHistory = historyDao.getHistory(FIRST_HISTORY_ID).first()
        assertThat(savedHistory, equalTo(null))
    }

    @Test
    fun daoUpdate_UpdateHistoryInDB() = runBlocking {
        val exerciseHistory = getExerciseHistory(FIRST_HISTORY_ID, FIRST_EXERCISE_ID)
        historyDao.insert(exerciseHistory)

        exerciseHistory.weight = NEW_WEIGHT
        historyDao.update(exerciseHistory)

        val savedHistory = historyDao.getHistory(FIRST_HISTORY_ID).first()
        assertThat(savedHistory.weight, equalTo(NEW_WEIGHT))
    }

    @Test
    fun daoDelete_DeleteAllHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        historyDao.deleteAllForExercise(FIRST_EXERCISE_ID)

        val savedHistory = historyDao.getFullExerciseHistory(FIRST_EXERCISE_ID).first()
        assertThat(savedHistory.size, equalTo(0))
    }

    private fun getExerciseHistory(id: Int, exerciseId: Int, time: LocalDate = LocalDate.now()) = ExerciseHistory(id, exerciseId, 10.0, 10, 10, time)

    private suspend fun addMultipleHistoryToDB() {
        historyDao.insert(getExerciseHistory(FIRST_HISTORY_ID, FIRST_EXERCISE_ID, LocalDate.now().minusDays(8)))
        historyDao.insert(getExerciseHistory(SECOND_HISTORY_ID, FIRST_EXERCISE_ID))
        historyDao.insert(getExerciseHistory(THIRD_HISTORY_ID, SECOND_EXERCISE_ID))
    }
}
