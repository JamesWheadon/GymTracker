package com.example.gymtracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.gymtracker.data.history.ExerciseHistory
import com.example.gymtracker.data.history.HistoryDao
import com.example.gymtracker.data.history.HistoryDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class HistoryDaoTest {

    private lateinit var historyDao: HistoryDao
    private lateinit var historyDatabase: HistoryDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        historyDatabase = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        historyDao = historyDatabase.historyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        historyDatabase.close()
    }

    @Test
    fun daoInsert_insertsExerciseHistoryIntoDB() = runBlocking {
        val exerciseHistory = ExerciseHistory(1, 1, 10, 10, 10, 10)
        historyDao.insert(exerciseHistory)
        val savedExerciseHistory = historyDao.getHistory(exerciseHistory.id).first()
        Assert.assertEquals(exerciseHistory, savedExerciseHistory)
    }
}
