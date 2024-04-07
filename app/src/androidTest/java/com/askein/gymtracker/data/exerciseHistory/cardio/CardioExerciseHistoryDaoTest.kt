package com.askein.gymtracker.data.exerciseHistory.cardio

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.askein.gymtracker.data.database.ExerciseWorkoutDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

class CardioExerciseHistoryDaoTest {

    private lateinit var cardioExerciseHistoryDao: CardioExerciseHistoryDao
    private lateinit var exerciseWorkoutDatabase: ExerciseWorkoutDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        exerciseWorkoutDatabase =
            Room.inMemoryDatabaseBuilder(context, ExerciseWorkoutDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        cardioExerciseHistoryDao = exerciseWorkoutDatabase.cardioExerciseHistoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        exerciseWorkoutDatabase.close()
    }

    @Test
    fun daoInsertAndDaoSelect_insertsCardioExerciseHistoryIntoDBAndRetrievesById() = runBlocking {
        val cardioExerciseHistory = createCardioExerciseHistory(1, 1)
        cardioExerciseHistoryDao.insert(cardioExerciseHistory)

        val savedCardioExerciseHistory = cardioExerciseHistoryDao.getHistory(cardioExerciseHistory.id).first()

        assertEquals(cardioExerciseHistory, savedCardioExerciseHistory)
    }

    @Test
    fun daoDelete_DeleteHistoryFromDB() = runBlocking {
        val exerciseHistory = createCardioExerciseHistory(1, 1)
        cardioExerciseHistoryDao.insert(exerciseHistory)

        cardioExerciseHistoryDao.delete(exerciseHistory)

        val savedHistory = cardioExerciseHistoryDao.getHistory(1).first()
        assertThat(savedHistory, equalTo(null))
    }

    @Test
    fun daoUpdate_UpdateHistoryInDB() = runBlocking {
        val exerciseHistory = createCardioExerciseHistory(1, 1)
        cardioExerciseHistoryDao.insert(exerciseHistory)

        val updateExerciseHistory = createCardioExerciseHistory(1, 1, 60)
        cardioExerciseHistoryDao.update(updateExerciseHistory)

        val savedHistory = cardioExerciseHistoryDao.getHistory(1).first()
        assertThat(savedHistory.minutes, equalTo(60))
    }

    @Test
    fun daoDelete_DeleteAllHistoryForExerciseFromDB() = runBlocking {
        addMultipleCardioHistoryToDB()

        cardioExerciseHistoryDao.deleteAllForExercise(1)

        val firstHistory = cardioExerciseHistoryDao.getHistory(1).first()
        val secondHistory = cardioExerciseHistoryDao.getHistory(2).first()
        assertThat(firstHistory, equalTo(null))
        assertThat(secondHistory, equalTo(null))
    }

    @Test
    fun daoDelete_DeleteAllHistoryForWorkoutHistoryFromDB() = runBlocking {
        addMultipleCardioHistoryToDB()

        cardioExerciseHistoryDao.deleteAllForWorkoutHistory(1)

        val firstHistory = cardioExerciseHistoryDao.getHistory(1).first()
        val secondHistory = cardioExerciseHistoryDao.getHistory(2).first()
        assertThat(firstHistory, notNullValue())
        assertThat(secondHistory, equalTo(null))
    }

    private fun createCardioExerciseHistory(id: Int, exerciseId: Int, minutes: Int = 60, workoutHistoryId: Int? = null) = CardioExerciseHistory(id, exerciseId, date = LocalDate.now(), minutes = minutes, workoutHistoryId = workoutHistoryId)

    private suspend fun addMultipleCardioHistoryToDB() {
        cardioExerciseHistoryDao.insert(createCardioExerciseHistory(1, 1))
        cardioExerciseHistoryDao.insert(createCardioExerciseHistory(2, 1, workoutHistoryId = 1))
        cardioExerciseHistoryDao.insert(createCardioExerciseHistory(3, 2))
    }
}