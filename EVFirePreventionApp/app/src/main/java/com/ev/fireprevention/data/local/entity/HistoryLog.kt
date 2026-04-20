package com.ev.fireprevention.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_logs")
data class HistoryLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val soc: Float,
    val soh: Float,
    val packVoltage: Double,
    val packCurrent: Double,
    val temperature: Double,
    val type: String = "CYCLE" // CYCLE, ALERT, TRIP
)
