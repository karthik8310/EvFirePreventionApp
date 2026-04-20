package com.ev.fireprevention.data.repository

import com.ev.fireprevention.data.local.dao.HistoryDao
import com.ev.fireprevention.data.local.entity.HistoryLog
import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val historyDao: HistoryDao) {

    val allLogs: Flow<List<HistoryLog>> = historyDao.getAllLogs()

    fun getLogsInRange(startTime: Long, endTime: Long): Flow<List<HistoryLog>> {
        return historyDao.getLogsInRange(startTime, endTime)
    }

    suspend fun insertLog(log: HistoryLog) {
        historyDao.insertLog(log)
    }

    suspend fun insertLogs(logs: List<HistoryLog>) {
        historyDao.insertLogs(logs)
    }
    
    suspend fun clearAll() {
        historyDao.clearAll()
    }
}
