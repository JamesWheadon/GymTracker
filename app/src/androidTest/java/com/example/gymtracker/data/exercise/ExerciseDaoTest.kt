package com.example.gymtracker.data.exercise

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ExerciseDaoTest {

    private lateinit var exerciseDao: ExerciseDao
    private lateinit var exerciseDatabase: ExerciseDatabase

    private val newExerciseName = "Press"
    private val exercise1 = Exercise(1, "Curls", "Biceps", "Dumbbells")
    private val exercise2 = Exercise(2, "Dips", "Triceps", "Dumbbells")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        exerciseDatabase = Room.inMemoryDatabaseBuilder(context, ExerciseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        exerciseDao = exerciseDatabase.exerciseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        exerciseDatabase.close()
    }

    @Test
    fun daoInsert_insertsExerciseIntoDB() = runBlocking {
        exerciseDao.insert(exercise1)

        val savedExercise = exerciseDao.getExercise(exercise1.id).first()

        assertThat(exercise1, equalTo(savedExercise))
    }

    @Test
    fun daoSelectAll_RetrieveMultipleExerciseFromDB() = runBlocking {
        exerciseDao.insert(exercise1)
        exerciseDao.insert(exercise2)

        val savedExercise = exerciseDao.getAllExercises().first()

        assertThat(exercise1, equalTo(savedExercise[0]))
        assertThat(exercise2, equalTo(savedExercise[1]))
    }

    @Test
    fun daoSelectByMuscleGroup_RetrieveMultipleExerciseFromDB() = runBlocking {
        exerciseDao.insert(exercise1)
        exerciseDao.insert(exercise2)

        val savedExercise = exerciseDao.getAllExercisesByMuscleGroup(exercise1.muscleGroup).first()

        assertThat(exercise1, equalTo(savedExercise[0]))
        assertThat(savedExercise.size, equalTo(1))
    }

    @Test
    fun daoDelete_DeleteExerciseFromDB() = runBlocking {
        exerciseDao.insert(exercise1)
        exerciseDao.delete(exercise1)

        val savedExercise = exerciseDao.getExercise(exercise1.id).first()

        assertThat(savedExercise, equalTo(null))
    }

    @Test
    fun daoUpdate_UpdateExerciseInDB() = runBlocking {
        exerciseDao.insert(exercise1)
        exercise1.name = newExerciseName

        exerciseDao.update(exercise1)

        val savedExercise = exerciseDao.getExercise(exercise1.id).first()
        assertThat(savedExercise.name, equalTo(newExerciseName))
    }

    @Test
    fun daoGetMuscleGroups_GetAllMuscleGroupsInDB() = runBlocking {
        exerciseDao.insert(exercise1)
        exerciseDao.insert(exercise2)

        val muscleGroups = exerciseDao.getAllMuscleGroups().first()

        assertThat(muscleGroups.size, equalTo(2))
        assertThat(muscleGroups, contains("Biceps", "Triceps"))
    }

    @Test
    fun daoGetExerciseNames_GetAllExerciseNamesInDB() = runBlocking {
        exerciseDao.insert(exercise1)
        exerciseDao.insert(exercise2)

        val exerciseNames = exerciseDao.getAllExerciseNames().first()

        assertThat(exerciseNames.size, equalTo(2))
        assertThat(exerciseNames, contains("Curls", "Dips"))
    }
}
