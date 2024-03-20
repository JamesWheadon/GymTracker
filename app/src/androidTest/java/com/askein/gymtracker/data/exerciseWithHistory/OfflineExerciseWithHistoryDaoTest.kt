package com.askein.gymtracker.data.exerciseWithHistory

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.askein.gymtracker.data.database.ExerciseWorkoutDatabase
import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.exercise.ExerciseDao
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryDao
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class OfflineExerciseWithHistoryDaoTest {

    private lateinit var exerciseDao: ExerciseDao
    private lateinit var weightsExerciseHistoryDao: WeightsExerciseHistoryDao
    private lateinit var cardioExerciseHistoryDao: CardioExerciseHistoryDao
    private lateinit var exerciseWithHistoryDao: ExerciseWithHistoryDao
    private lateinit var exerciseWorkoutDatabase: ExerciseWorkoutDatabase

    private val exercise = Exercise(1, "test exercise", "muscle", "kit")
    private val weightsExerciseHistory =
        WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now(), 1, 1)
    private val cardioExerciseHistory =
        CardioExerciseHistory(1, 1, LocalDate.now(), distance = 1.0, workoutHistoryId = 1)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        exerciseWorkoutDatabase =
            Room.inMemoryDatabaseBuilder(context, ExerciseWorkoutDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        exerciseDao = exerciseWorkoutDatabase.exerciseDao()
        weightsExerciseHistoryDao = exerciseWorkoutDatabase.weightsExerciseHistoryDao()
        cardioExerciseHistoryDao = exerciseWorkoutDatabase.cardioExerciseHistoryDao()
        exerciseWithHistoryDao = exerciseWorkoutDatabase.exerciseWithHistoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        exerciseWorkoutDatabase.close()
    }

    @Test
    fun daoSelectByExerciseId_retrievesExerciseWithHistoryFromDB() = runBlocking {
        exerciseDao.insert(exercise)
        weightsExerciseHistoryDao.insert(weightsExerciseHistory)
        cardioExerciseHistoryDao.insert(cardioExerciseHistory)

        val exerciseWithHistory = exerciseWithHistoryDao.getExerciseWithHistory(exercise.exerciseId).first()

        assertThat(exerciseWithHistory.exercise, equalTo(exercise))
        assertThat(exerciseWithHistory.weightsHistory.size, equalTo(1))
        assertThat(exerciseWithHistory.cardioHistory.size, equalTo(1))
        assertThat(exerciseWithHistory.weightsHistory[0], equalTo(weightsExerciseHistory))
        assertThat(exerciseWithHistory.cardioHistory[0], equalTo(cardioExerciseHistory))
    }
}
