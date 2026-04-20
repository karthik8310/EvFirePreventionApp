package com.ev.fireprevention.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ev.fireprevention.data.local.entity.HistoryLog
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HistoryLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<HistoryLog>)

    @Query("SELECT * FROM history_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<HistoryLog>>

    @Query("SELECT * FROM history_logs WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp ASC")
    fun getLogsInRange(startTime: Long, endTime: Long): Flow<List<HistoryLog>>

    @Query("DELETE FROM history_logs")
    suspend fun clearAll()
}
