package com.example.gymtracker.data.workout

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtracker.data.database.ExerciseDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WorkoutDaoTest {

    private lateinit var workoutDao: WorkoutDao
    private lateinit var exerciseDatabase: ExerciseDatabase

    private val workout = Workout(1, "test workout")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        exerciseDatabase = Room.inMemoryDatabaseBuilder(context, ExerciseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        workoutDao = exerciseDatabase.workoutDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        exerciseDatabase.close()
    }

    @Test
    fun daoInsert_insertsWorkoutIntoDB() = runBlocking {
        workoutDao.insert(workout)

        val savedWorkout = workoutDao.getAllWorkouts().first()[0]

        MatcherAssert.assertThat(workout, equalTo(savedWorkout))
    }
}
