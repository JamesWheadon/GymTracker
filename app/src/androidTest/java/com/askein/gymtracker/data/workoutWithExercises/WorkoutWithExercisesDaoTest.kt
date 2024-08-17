package com.askein.gymtracker.data.workoutWithExercises

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRef
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefDao
import com.askein.gymtracker.data.workoutHistory.WorkoutHistory
import com.askein.gymtracker.data.workoutHistory.WorkoutHistoryDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class WorkoutWithExercisesDaoTest {

    private lateinit var workoutDao: WorkoutDao
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var weightsExerciseHistoryDao: WeightsExerciseHistoryDao
    private lateinit var cardioExerciseHistoryDao: CardioExerciseHistoryDao
    private lateinit var workoutHistoryDao: WorkoutHistoryDao
    private lateinit var workoutExerciseCrossRefDao: WorkoutExerciseCrossRefDao
    private lateinit var workoutWithExercisesDao: WorkoutWithExercisesDao
    private lateinit var exerciseWorkoutDatabase: ExerciseWorkoutDatabase

    private val workout = Workout(1, "test workout")
    private val weightsExercise = Exercise(1, ExerciseType.WEIGHTS,"test exercise", "muscle", "kit")
    private val cardioExercise = Exercise(2, ExerciseType.CARDIO, "Treadmill", "", "")
    private val weightsExerciseHistory = WeightsExerciseHistory(1, 1, listOf(1.0), 1, listOf(1), LocalDate.now(), 1, null, 1)
    private val cardioExerciseHistory = CardioExerciseHistory(1, 2, LocalDate.now(), 1, 1, 1, 1.0, 1)
    private val workoutHistory = WorkoutHistory(1, 1, LocalDate.now())
    private val weightsCrossRef = WorkoutExerciseCrossRef(1, 1, 0)
    private val cardioCrossRef = WorkoutExerciseCrossRef(1, 2, 1)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        exerciseWorkoutDatabase = Room.inMemoryDatabaseBuilder(context, ExerciseWorkoutDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        workoutDao = exerciseWorkoutDatabase.workoutDao()
        exerciseDao = exerciseWorkoutDatabase.exerciseDao()
        weightsExerciseHistoryDao = exerciseWorkoutDatabase.weightsExerciseHistoryDao()
        cardioExerciseHistoryDao = exerciseWorkoutDatabase.cardioExerciseHistoryDao()
        workoutHistoryDao = exerciseWorkoutDatabase.workoutHistoryDao()
        workoutExerciseCrossRefDao = exerciseWorkoutDatabase.workoutExerciseCrossRefDao()
        workoutWithExercisesDao = exerciseWorkoutDatabase.workoutWithExercisesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        exerciseWorkoutDatabase.close()
    }

    @Test
    fun daoSelectByWorkoutId_retrievesWorkoutFromDB() = runBlocking {
        workoutDao.insert(workout)
        exerciseDao.insert(weightsExercise)
        exerciseDao.insert(cardioExercise)
        weightsExerciseHistoryDao.insert(weightsExerciseHistory)
        cardioExerciseHistoryDao.insert(cardioExerciseHistory)
        workoutHistoryDao.insert(workoutHistory)
        workoutExerciseCrossRefDao.insert(cardioCrossRef)
        workoutExerciseCrossRefDao.insert(weightsCrossRef)

        val savedWorkout = workoutWithExercisesDao.getWorkoutWithExercises(workout.workoutId).first()

        assertThat(savedWorkout.workout, equalTo(workout))
        assertThat(savedWorkout.exercises.size, equalTo(2))
        assertThat(savedWorkout.exercises.contains(weightsExercise), equalTo(true))
        assertThat(savedWorkout.exercises.contains(cardioExercise), equalTo(true))
        assertThat(savedWorkout.workoutHistory.size, equalTo(1))
        assertThat(savedWorkout.workoutHistory[0].weightsExercises.size, equalTo(1))
        assertThat(savedWorkout.workoutHistory[0].cardioExercises.size, equalTo(1))
        assertThat(savedWorkout.exerciseOrder.sortedBy { it.order }, equalTo(listOf(weightsCrossRef, cardioCrossRef)))
    }
}
