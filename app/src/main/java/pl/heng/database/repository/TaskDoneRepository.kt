package pl.heng.database.repository

import android.app.Application
import kotlinx.coroutines.runBlocking
import pl.heng.database.DatabaseHeng
import pl.heng.database.dao.TaskHistoryDao
import pl.heng.database.model.TaskDoneHistory
import pl.heng.database.model.IModel
import java.time.LocalDate

class TaskDoneRepository(application: Application) : Repository {

    private var taskDoneDao: TaskHistoryDao
    private var allTaskDones: List<TaskDoneHistory>

    init {
        val database = DatabaseHeng.GetDatabase(application.applicationContext)
        taskDoneDao = database.habitDoneDao()
        allTaskDones = runBlocking { taskDoneDao.getHabitHistoryList() }
    }

    override suspend fun insert(model: IModel) {
        taskDoneDao.insertHabitDone(model as TaskDoneHistory)
    }

    suspend fun delete(habitDoneId: Int) {
        taskDoneDao.deleteHabitDone(habitDoneId)
    }

    suspend fun getDoneCount(habitDoneId: Int) = taskDoneDao.getHabitDoneCount(habitDoneId)

    suspend fun getHabitDone(habitDoneId: Int) = taskDoneDao.getHabitDoneHistory(habitDoneId)

    suspend fun getHabitDoneToday(habitId : Int, date : LocalDate) = (taskDoneDao.getTodayDone(habitId,date.toString()) == 1)
}