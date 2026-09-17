package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SessionType {
    FOCUS,
    SHORT_BREAK,
    LONG_BREAK
}

@Entity(tableName = "pomodoro_sessions")
data class SessionRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionType: SessionType,
    val durationSeconds: Int,
    val completedAt: Long = System.currentTimeMillis(),
    val taskTitle: String? = null,
    val isCompletedNormally: Boolean = true
)

@Entity(tableName = "tasks")
data class TaskItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val estimatedPomodoros: Int = 1,
    val completedPomodoros: Int = 0,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val category: String = "Général"
)
