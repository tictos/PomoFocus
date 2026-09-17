package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SessionRecord
import com.example.data.model.TaskItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroDao {
    // Sessions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionRecord): Long

    @Query("SELECT * FROM pomodoro_sessions ORDER BY completedAt DESC")
    fun getAllSessions(): Flow<List<SessionRecord>>

    @Query("SELECT * FROM pomodoro_sessions WHERE completedAt >= :startEpochMillis ORDER BY completedAt ASC")
    fun getSessionsSince(startEpochMillis: Long): Flow<List<SessionRecord>>

    @Query("SELECT * FROM pomodoro_sessions WHERE sessionType = 'FOCUS' AND completedAt >= :startEpochMillis")
    suspend fun getFocusSessionsSince(startEpochMillis: Long): List<SessionRecord>

    @Query("SELECT SUM(durationSeconds) FROM pomodoro_sessions WHERE sessionType = 'FOCUS'")
    fun getTotalFocusSeconds(): Flow<Long?>

    @Query("DELETE FROM pomodoro_sessions WHERE id = :id")
    suspend fun deleteSessionById(id: Long)

    @Query("DELETE FROM pomodoro_sessions")
    suspend fun clearAllSessions()

    // Tasks
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskItem): Long

    @Update
    suspend fun updateTask(task: TaskItem)

    @Delete
    suspend fun deleteTask(task: TaskItem)

    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, createdAt DESC")
    fun getAllTasks(): Flow<List<TaskItem>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskItem?

    @Query("UPDATE tasks SET completedPomodoros = completedPomodoros + 1 WHERE id = :taskId")
    suspend fun incrementTaskPomodoros(taskId: Long)
}
