package com.example.gymtracker

import android.content.Context
import android.icu.util.Calendar
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.gymtracker.data.history.ExerciseHistory
import com.example.gymtracker.data.history.HistoryDao
import com.example.gymtracker.data.history.HistoryDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Date

class HistoryDaoTest {

    private lateinit var historyDao: HistoryDao
    private lateinit var historyDatabase: HistoryDatabase

    private var calendar = Calendar.getInstance()
    private var newWeight = 20

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
        val exerciseHistory = getExerciseHistory(1, 1)
        historyDao.insert(exerciseHistory)

        val savedExerciseHistory = historyDao.getHistory(exerciseHistory.id).first()

        Assert.assertEquals(exerciseHistory, savedExerciseHistory)
    }

    @Test
    fun daoSelectAllByExercise_RetrieveFullHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        val savedExerciseHistory = historyDao.getFullExerciseHistory(1).first()

        assertThat(savedExerciseHistory.size, equalTo(2))
    }

    @Test
    fun daoSelectMostRecentExercise_RetrieveLatestHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        val savedExerciseHistory = historyDao.getLatestExerciseHistory(1).first()

        assertThat(savedExerciseHistory, equalTo(getExerciseHistory(2, 1)))
    }

    @Test
    fun daoSelectExerciseInPastWeek_RetrieveRecentHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        val savedExerciseHistory = historyDao.getRecentExerciseHistory(1, 7).first()

        assertThat(savedExerciseHistory.size, equalTo(1))
    }

    @Test
    fun daoSelectExerciseInPastMonth_RetrieveRecentHistoryForExerciseFromDB() = runBlocking {
        addMultipleHistoryToDB()

        val savedExerciseHistory = historyDao.getRecentExerciseHistory(1, 31).first()

        assertThat(savedExerciseHistory.size, equalTo(2))
    }

    @Test
    fun daoDelete_DeleteHistoryFromDB() = runBlocking {
        val exerciseHistory = getExerciseHistory(1, 1)
        historyDao.insert(exerciseHistory)

        historyDao.delete(exerciseHistory)

        val savedHistory = historyDao.getHistory(1).first()
        assertThat(savedHistory, equalTo(null))
    }

    @Test
    fun daoUpdate_UpdateHistoryInDB() = runBlocking {
        val exerciseHistory = getExerciseHistory(1, 1)
        historyDao.insert(exerciseHistory)

        exerciseHistory.weight = newWeight
        historyDao.update(exerciseHistory)

        val savedHistory = historyDao.getHistory(1).first()
        assertThat(savedHistory.weight, equalTo(newWeight))
    }

    private fun getExerciseHistory(id: Int, exerciseId: Int, time: Date = calendar.time) = ExerciseHistory(id, exerciseId, 10, 10, 10, 10, time)

    private suspend fun addMultipleHistoryToDB() {
        historyDao.insert(getExerciseHistory(1, 1, Date(calendar.time.time - 8 * 604800000L)))
        historyDao.insert(getExerciseHistory(2, 1))
        historyDao.insert(getExerciseHistory(3, 2))
    }
}
