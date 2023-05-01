package com.example.gymtracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtracker.data.Exercise
import com.example.gymtracker.data.ExerciseDao
import com.example.gymtracker.data.ExerciseDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ExerciseDaoTest {

    private lateinit var exerciseDao: ExerciseDao
    private lateinit var exerciseDatabase: ExerciseDatabase

    private val exercise1 = Exercise(1, "Curls", "Biceps")
    private val exercise2 = Exercise(2, "Dips", "Triceps")

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
        assertEquals(exercise1, savedExercise)
    }

    @Test
    fun daoSelectAll_RetrieveMultipleExerciseFromDB() = runBlocking {
        exerciseDao.insert(exercise1)
        exerciseDao.insert(exercise2)
        val savedExercise = exerciseDao.getAllExercises().first()
        assertEquals(exercise1, savedExercise[0])
        assertEquals(exercise2, savedExercise[1])
    }

    @Test
    fun daoSelectByMuscleGroup_RetrieveMultipleExerciseFromDB() = runBlocking {
        exerciseDao.insert(exercise1)
        exerciseDao.insert(exercise2)
        val savedExercise = exerciseDao.getAllExercisesByMuscleGroup(exercise1.muscleGroup).first()
        assertEquals(exercise1, savedExercise[0])
        assertEquals(1, savedExercise.size)
    }

    @Test
    fun daoDelete_DeleteExerciseFromDB() = runBlocking {
        exerciseDao.insert(exercise1)
        exerciseDao.delete(exercise1)
        val savedExercise = exerciseDao.getExercise(exercise1.id).first()
        assertEquals(null, savedExercise)
    }

    @Test
    fun daoUpdate_UpdateExerciseInDB() = runBlocking {
        exerciseDao.insert(exercise1)
        val newExerciseName = "Press"
        exercise1.name = newExerciseName
        exerciseDao.update(exercise1)
        val savedExercise = exerciseDao.getExercise(exercise1.id).first()
        assertEquals(newExerciseName, savedExercise.name)
    }
}
