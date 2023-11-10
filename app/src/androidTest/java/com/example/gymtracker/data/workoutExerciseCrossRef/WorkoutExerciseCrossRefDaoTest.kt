package com.example.gymtracker.data.workoutExerciseCrossRef

import android.content.Context
import androidx.room.Room
import androidx.room.RoomSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtracker.data.database.ExerciseDatabase
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WorkoutExerciseCrossRefDaoTest {

    private lateinit var workoutExerciseCrossRefDao: WorkoutExerciseCrossRefDao
    private lateinit var exercise: ExerciseDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        exercise = Room.inMemoryDatabaseBuilder(context, ExerciseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        workoutExerciseCrossRefDao = exercise.workoutExerciseCrossRefDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        exercise.close()
    }

    @Test
    fun daoInsert_InsertsWorkoutExerciseIntoDB() = runBlocking {
        workoutExerciseCrossRefDao.insert(WorkoutExerciseCrossRef(1, 1))

        val query = RoomSQLiteQuery.acquire("SELECT * FROM workoutsExercises", 0)
        val saved = exercise.query(query)
        saved.moveToFirst()

        val workoutIdIndex = saved.getColumnIndex("workoutId")
        val exerciseIdIndex = saved.getColumnIndex("exerciseId")

        assertThat(saved.count, equalTo(1))
        assertThat(saved.getInt(workoutIdIndex), equalTo(1))
        assertThat(saved.getInt(exerciseIdIndex), equalTo(1))
        saved.close()
    }

    @Test
    fun daoDelete_DeletesWorkoutExerciseFromDB() = runBlocking {
        workoutExerciseCrossRefDao.insert(WorkoutExerciseCrossRef(1, 1))
        workoutExerciseCrossRefDao.delete(WorkoutExerciseCrossRef(1, 1))

        val query = RoomSQLiteQuery.acquire("SELECT * FROM workoutsExercises", 0)
        val saved = exercise.query(query)

        assertThat(saved.count, equalTo(0))
        saved.close()
    }

    @Test
    fun daoDelete_DeletesAllWorkoutExerciseFromDBForWorkout() = runBlocking {
        workoutExerciseCrossRefDao.insert(WorkoutExerciseCrossRef(1, 1))
        workoutExerciseCrossRefDao.insert(WorkoutExerciseCrossRef(1, 2))
        workoutExerciseCrossRefDao.deleteAllCrossRefForWorkout(1)

        val query = RoomSQLiteQuery.acquire("SELECT * FROM workoutsExercises", 0)
        val saved = exercise.query(query)

        assertThat(saved.count, equalTo(0))
        saved.close()
    }

    @Test
    fun daoDelete_DeletesAllWorkoutExerciseFromDBForExercise() = runBlocking {
        workoutExerciseCrossRefDao.insert(WorkoutExerciseCrossRef(1, 1))
        workoutExerciseCrossRefDao.insert(WorkoutExerciseCrossRef(2, 1))
        workoutExerciseCrossRefDao.deleteAllCrossRefForExercise(1)

        val query = RoomSQLiteQuery.acquire("SELECT * FROM workoutsExercises", 0)
        val saved = exercise.query(query)

        assertThat(saved.count, equalTo(0))
        saved.close()
    }
}
