package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.PomodoroDao
import com.example.data.model.SessionRecord
import com.example.data.model.TaskItem

@Database(entities = [SessionRecord::class, TaskItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pomodoroDao(): PomodoroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chronoglass_pomodoro.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
