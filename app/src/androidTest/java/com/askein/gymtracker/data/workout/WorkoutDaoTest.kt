package com.askein.gymtracker.data.workout

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.askein.gymtracker.data.database.ExerciseWorkoutDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

private const val UPDATED_NAME = "updated name"

@RunWith(AndroidJUnit4::class)
class WorkoutDaoTest {

    private lateinit var workoutDao: WorkoutDao
    private lateinit var exerciseWorkoutDatabase: ExerciseWorkoutDatabase

    private val workout = Workout(1, "test workout")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        exerciseWorkoutDatabase = Room.inMemoryDatabaseBuilder(context, ExerciseWorkoutDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        workoutDao = exerciseWorkoutDatabase.workoutDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        exerciseWorkoutDatabase.close()
    }

    @Test
    fun daoInsert_insertsWorkoutIntoDB() = runBlocking {
        workoutDao.insert(workout)

        val savedWorkout = workoutDao.getAllWorkouts().first()[0]

        assertThat(workout, equalTo(savedWorkout))
    }

    @Test
    fun daoUpdate_UpdateWorkoutInDB() = runBlocking {
        workoutDao.insert(workout)
        workout.name = UPDATED_NAME

        workoutDao.update(workout)

        val savedWorkout = workoutDao.getAllWorkouts().first()[0]
        assertThat(savedWorkout.name, equalTo(UPDATED_NAME))
    }

    @Test
    fun daoDelete_DeleteWorkoutFromDB() = runBlocking {
        workoutDao.insert(workout)
        workoutDao.delete(workout)

        val savedWorkout = workoutDao.getAllWorkouts().first()

        assertThat(savedWorkout, equalTo(listOf()))
    }
}
