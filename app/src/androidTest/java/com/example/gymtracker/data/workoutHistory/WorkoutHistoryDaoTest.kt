package com.example.gymtracker.data.workoutHistory

import android.content.Context
import androidx.room.Room
import androidx.room.RoomSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtracker.data.database.ExerciseWorkoutDatabase
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
class WorkoutHistoryDaoTest {

    private lateinit var workoutHistoryDao: WorkoutHistoryDao
    private lateinit var database: ExerciseWorkoutDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, ExerciseWorkoutDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        workoutHistoryDao = database.workoutHistoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun daoInsert_InsertsWorkoutExerciseIntoDB() = runBlocking {
        val insertId = workoutHistoryDao.insert(WorkoutHistory(workoutId = 1, date = LocalDate.now()))

        val query = RoomSQLiteQuery.acquire("SELECT * FROM workout_history", 0)
        val saved = database.query(query)
        saved.moveToFirst()

        val workoutIdIndex = saved.getColumnIndex("workoutId")
        val idIndex = saved.getColumnIndex("workoutHistoryId")

        assertThat(saved.count, equalTo(1))
        assertThat(saved.getInt(workoutIdIndex), equalTo(1))
        assertThat(saved.getInt(idIndex), equalTo(1))
        assertThat(insertId, equalTo(1))
        saved.close()
    }
}
