package com.example.gymtracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gymtracker.converters.LocalDateConverter
import com.example.gymtracker.data.history.ExerciseHistory
import com.example.gymtracker.data.history.HistoryDao

@Database(entities = [ExerciseHistory::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateConverter::class)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var Instance: HistoryDatabase? = null

        fun getDatabase(context: Context): HistoryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    HistoryDatabase::class.java,
                    "history_database"
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
