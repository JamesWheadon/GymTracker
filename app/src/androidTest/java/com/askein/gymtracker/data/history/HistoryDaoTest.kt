package com.askein.gymtracker.data.history

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.askein.gymtracker.data.database.ExerciseWorkoutDatabase
import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.exercise.ExerciseDao
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryDao
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryDao
import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.data.workout.WorkoutDao
import com.askein.gymtracker.data.workoutHistory.WorkoutHistory
import com.askein.gymtracker.data.workoutHistory.WorkoutHistoryDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

class HistoryDaoTest {

    private lateinit var historyDao: HistoryDao
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var workoutDao: WorkoutDao
    private lateinit var workoutHistoryDao: WorkoutHistoryDao
    private lateinit var cardioExerciseHistoryDao: CardioExerciseHistoryDao
    private lateinit var weightExerciseHistoryDao: WeightsExerciseHistoryDao
    private lateinit var exerciseWorkoutDatabase: ExerciseWorkoutDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        exerciseWorkoutDatabase =
            Room.inMemoryDatabaseBuilder(context, ExerciseWorkoutDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        historyDao = exerciseWorkoutDatabase.historyDao()
        exerciseDao = exerciseWorkoutDatabase.exerciseDao()
        workoutDao = exerciseWorkoutDatabase.workoutDao()
        workoutHistoryDao = exerciseWorkoutDatabase.workoutHistoryDao()
        cardioExerciseHistoryDao = exerciseWorkoutDatabase.cardioExerciseHistoryDao()
        weightExerciseHistoryDao = exerciseWorkoutDatabase.weightsExerciseHistoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        exerciseWorkoutDatabase.close()
    }

    @Test
    fun daoGetWorkoutHistoryDates_getsDatesFromDB() = runBlocking {
        workoutHistoryDao.insert(WorkoutHistory(0, 0, LocalDate.now()))
        workoutHistoryDao.insert(WorkoutHistory(0, 0, LocalDate.now().minusDays(1)))

        val dates = historyDao.getWorkoutHistoryDates().first()

        assertThat(dates.size, equalTo(2))
        assertThat(
            dates[0],
            anyOf(
                equalTo(LocalDate.now().toEpochDay()),
                equalTo(LocalDate.now().minusDays(1).toEpochDay())
            )
        )
        assertThat(
            dates[1],
            anyOf(
                equalTo(LocalDate.now().toEpochDay()),
                equalTo(LocalDate.now().minusDays(1).toEpochDay())
            )
        )
    }

    @Test
    fun daoGetCardioHistoryDates_getsDatesFromDB() = runBlocking {
        cardioExerciseHistoryDao.insert(createCardioHistory(LocalDate.now(), null, 0))
        cardioExerciseHistoryDao.insert(createCardioHistory(LocalDate.now().minusDays(1), 1, 0))
        cardioExerciseHistoryDao.insert(createCardioHistory(LocalDate.now().minusDays(2), null, 0))

        val dates = historyDao.getCardioHistoryDates().first()

        assertThat(dates.size, equalTo(2))
        assertThat(
            dates[0],
            anyOf(
                equalTo(LocalDate.now().toEpochDay()),
                equalTo(LocalDate.now().minusDays(2).toEpochDay())
            )
        )
        assertThat(
            dates[1],
            anyOf(
                equalTo(LocalDate.now().toEpochDay()),
                equalTo(LocalDate.now().minusDays(2).toEpochDay())
            )
        )
    }

    @Test
    fun daoGetWeightsHistoryDates_getsDatesFromDB() = runBlocking {
        weightExerciseHistoryDao.insert(createWeightsHistory(LocalDate.now(), null, 0))
        weightExerciseHistoryDao.insert(createWeightsHistory(LocalDate.now().minusDays(1), 1, 0))
        weightExerciseHistoryDao.insert(createWeightsHistory(LocalDate.now().minusDays(2), null, 0))

        val dates = historyDao.getWeightsHistoryDates().first()

        assertThat(dates.size, equalTo(2))
        assertThat(
            dates[0],
            anyOf(
                equalTo(LocalDate.now().toEpochDay()),
                equalTo(LocalDate.now().minusDays(2).toEpochDay())
            )
        )
        assertThat(
            dates[1],
            anyOf(
                equalTo(LocalDate.now().toEpochDay()),
                equalTo(LocalDate.now().minusDays(2).toEpochDay())
            )
        )
    }

    @Test
    fun daoGetWorkoutsForDate_getsWorkoutsFromDB() = runBlocking {
        workoutDao.insert(Workout(name = "Arms"))
        workoutDao.insert(Workout(name = "Legs"))
        workoutHistoryDao.insert(WorkoutHistory(workoutId = 1, date = LocalDate.now()))
        workoutHistoryDao.insert(WorkoutHistory(workoutId = 1, date = LocalDate.now().minusDays(1)))
        workoutHistoryDao.insert(WorkoutHistory(workoutId = 2, date = LocalDate.now().minusDays(1)))

        val workouts = historyDao.getWorkoutsForDate(LocalDate.now().toEpochDay()).first()

        assertThat(workouts.size, equalTo(1))
        assertThat(workouts[0].name, equalTo("Arms"))
    }

    @Test
    fun daoGetExercisesForDate_getsExercisesFromDB() = runBlocking {
        exerciseDao.insert(Exercise(exerciseType = ExerciseType.CARDIO, name = "Treadmill", muscleGroup = "", equipment = ""))
        exerciseDao.insert(Exercise(exerciseType = ExerciseType.WEIGHTS, name = "Row", muscleGroup = "", equipment = ""))
        exerciseDao.insert(Exercise(exerciseType = ExerciseType.WEIGHTS, name = "Curls", muscleGroup = "", equipment = ""))
        exerciseDao.insert(Exercise(exerciseType = ExerciseType.WEIGHTS, name = "Bench", muscleGroup = "", equipment = ""))
        cardioExerciseHistoryDao.insert(createCardioHistory(LocalDate.now(), null, 1))
        cardioExerciseHistoryDao.insert(createCardioHistory(LocalDate.now(), 1, 2))
        cardioExerciseHistoryDao.insert(createCardioHistory(LocalDate.now().minusDays(2), null, 1))
        weightExerciseHistoryDao.insert(createWeightsHistory(LocalDate.now(), null, 3))
        weightExerciseHistoryDao.insert(createWeightsHistory(LocalDate.now(), 1, 4))
        weightExerciseHistoryDao.insert(createWeightsHistory(LocalDate.now().minusDays(2), null, 3))

        val exercises = historyDao.getExercisesForDate(LocalDate.now().toEpochDay()).first()

        assertThat(exercises.size, equalTo(2))
        assertThat(exercises[0].name, anyOf(equalTo("Treadmill"), equalTo("Curls")))
        assertThat(exercises[1].name, anyOf(equalTo("Treadmill"), equalTo("Curls")))
    }

    private fun createCardioHistory(
        date: LocalDate,
        workoutHistoryId: Int?,
        exerciseId: Int
    ): CardioExerciseHistory = CardioExerciseHistory(
        exerciseId = exerciseId,
        date = date,
        workoutHistoryId = workoutHistoryId
    )

    private fun createWeightsHistory(
        date: LocalDate,
        workoutHistoryId: Int?,
        exerciseId: Int
    ): WeightsExerciseHistory = WeightsExerciseHistory(
        exerciseId = exerciseId,
        weight = 0.0,
        sets = 0,
        reps = 0,
        date = date,
        workoutHistoryId = workoutHistoryId
    )
}
