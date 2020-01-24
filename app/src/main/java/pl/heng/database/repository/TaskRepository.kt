package pl.heng.database.repository

import android.app.Application
import pl.heng.database.DatabaseHeng
import pl.heng.database.dao.HabitDao
import pl.heng.database.model.Task
import pl.heng.database.model.IModel

class TaskRepository(application: Application) : Repository {

    private var habitDao: HabitDao

    init {
        val database = DatabaseHeng.GetDatabase(application.applicationContext)
        habitDao = database.habitDao()
    }

    override suspend fun insert(model: IModel) {
        habitDao.insertHabit(model as Task)
    }

    fun getHabitList() = habitDao.getHabits()
}
