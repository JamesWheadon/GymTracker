package com.example.gymtracker.data.workoutWithExercises

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtracker.data.database.ExerciseDatabase
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseDao
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workout.WorkoutDao
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRef
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WorkoutWithExercisesDaoTest {

    private lateinit var workoutDao: WorkoutDao
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var workoutExerciseCrossRefDao: WorkoutExerciseCrossRefDao
    private lateinit var workoutWithExercisesDao: WorkoutWithExercisesDao
    private lateinit var exerciseDatabase: ExerciseDatabase

    private val workout = Workout(1, "test workout")
    private val exercise = Exercise(1, "test exercise", "muscle", "kit")
    private val crossRef = WorkoutExerciseCrossRef(1, 1)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        exerciseDatabase = Room.inMemoryDatabaseBuilder(context, ExerciseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        workoutDao = exerciseDatabase.workoutDao()
        exerciseDao = exerciseDatabase.exerciseDao()
        workoutExerciseCrossRefDao = exerciseDatabase.workoutExerciseCrossRefDao()
        workoutWithExercisesDao = exerciseDatabase.workoutWithExercisesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        exerciseDatabase.close()
    }

    @Test
    fun daoSelectByWorkoutId_retrievesWorkoutFromDB() = runBlocking {
        workoutDao.insert(workout)
        exerciseDao.insert(exercise)
        workoutExerciseCrossRefDao.insert(crossRef)

        val savedWorkout = workoutWithExercisesDao.getWorkoutWithExercises(workout.workoutId).first()

        assertThat(savedWorkout.workout, equalTo(workout))
        assertThat(savedWorkout.exercises.size, equalTo(1))
    }
}
