package com.example.data.repository

import com.example.data.dao.PomodoroDao
import com.example.data.model.SessionRecord
import com.example.data.model.SessionType
import com.example.data.model.TaskItem
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class PomodoroRepository(private val dao: PomodoroDao) {

    val allSessions: Flow<List<SessionRecord>> = dao.getAllSessions()
    val allTasks: Flow<List<TaskItem>> = dao.getAllTasks()
    val totalFocusSeconds: Flow<Long?> = dao.getTotalFocusSeconds()

    suspend fun recordSession(
        sessionType: SessionType,
        durationSeconds: Int,
        taskTitle: String? = null,
        isCompletedNormally: Boolean = true
    ): Long {
        val record = SessionRecord(
            sessionType = sessionType,
            durationSeconds = durationSeconds,
            taskTitle = taskTitle,
            isCompletedNormally = isCompletedNormally
        )
        return dao.insertSession(record)
    }

    fun getSessionsForPastDays(days: Int): Flow<List<SessionRecord>> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -days)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return dao.getSessionsSince(cal.timeInMillis)
    }

    suspend fun deleteSession(id: Long) = dao.deleteSessionById(id)
    suspend fun clearHistory() = dao.clearAllSessions()

    suspend fun insertTask(task: TaskItem) = dao.insertTask(task)
    suspend fun updateTask(task: TaskItem) = dao.updateTask(task)
    suspend fun deleteTask(task: TaskItem) = dao.deleteTask(task)
    suspend fun incrementTaskPomodoro(taskId: Long) = dao.incrementTaskPomodoros(taskId)
}
