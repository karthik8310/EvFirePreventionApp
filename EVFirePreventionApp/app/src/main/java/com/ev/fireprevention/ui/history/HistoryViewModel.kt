package com.ev.fireprevention.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ev.fireprevention.data.local.entity.HistoryLog
import com.ev.fireprevention.data.repository.HistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    val historyLogs: StateFlow<List<HistoryLog>> = repository.allLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val recentLogs: StateFlow<List<HistoryLog>> = repository.allLogs
        .map { logs -> logs.sortedBy { it.timestamp }.takeLast(20) } // Keep last 20 points (approx 1 min at 3s interval)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addDummyData() {
        viewModelScope.launch {
            val dummyLogs = List(20) { index ->
                HistoryLog(
                    timestamp = System.currentTimeMillis() - (index * 3600000L), // 1 hour intervals
                    soc = (20..100).random().toFloat(),
                    soh = (90..100).random().toFloat(),
                    packVoltage = (45..52).random().toDouble(),
                    packCurrent = (10..50).random().toDouble(),
                    temperature = (25..45).random().toDouble()
                )
            }
            repository.insertLogs(dummyLogs)
        }
    }
    
    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}

class HistoryViewModelFactory(private val repository: HistoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
