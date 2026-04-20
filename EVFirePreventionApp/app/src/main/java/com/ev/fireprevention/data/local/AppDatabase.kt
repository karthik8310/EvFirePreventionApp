package com.ev.fireprevention.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ev.fireprevention.data.local.dao.HistoryDao
import com.ev.fireprevention.data.local.entity.HistoryLog

@Database(entities = [HistoryLog::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ev_fire_prevention_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
