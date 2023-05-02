package com.example.gymtracker.data.history

import kotlinx.coroutines.flow.Flow

class OfflineHistoryRepository(private val historyDao: HistoryDao) : HistoryRepository {

    override fun getHistoryStream(id: Int): Flow<ExerciseHistory?> = historyDao.getHistory(id)

    override suspend fun insertHistory(history: ExerciseHistory) = historyDao.insert(history)
}
