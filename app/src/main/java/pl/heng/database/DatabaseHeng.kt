package pl.heng.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.heng.database.dao.HabitDao
import pl.heng.database.dao.TaskHistoryDao
import pl.heng.database.model.Task
import pl.heng.database.model.TaskDoneHistory

@Database(entities = arrayOf(Task::class,TaskDoneHistory::class), version = 1, exportSchema = false)
abstract class DatabaseHeng : RoomDatabase() {

    abstract fun habitDao() : HabitDao
    abstract fun habitDoneDao() : TaskHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseHeng? = null

        fun GetDatabase(
            context: Context): DatabaseHeng {
            return INSTANCE ?: synchronized(this) {
                val tempInstance = INSTANCE

                if (tempInstance != null) {
                    return tempInstance
                }
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHeng::class.java,
                    "DatabaseHeng"
                )
                .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}