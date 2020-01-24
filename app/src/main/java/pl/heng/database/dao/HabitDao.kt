package pl.heng.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.heng.database.model.Task

@Dao
interface HabitDao {
    @Query("SELECT * FROM Task ORDER BY createDate DESC")
    fun getHabits() : LiveData<List<Task>>

    @Query("SELECT * FROM Task WHERE uid = :idHabit ")
    suspend fun getHabitById(idHabit : Int) : Task

    @Query("SELECT * FROM Task WHERE name = :name")
    suspend fun getHabitByName(name : String) : Task

    @Insert
    suspend fun insertHabit(task: Task)

    @Delete
    suspend fun deleteHabit(task: Task)
}