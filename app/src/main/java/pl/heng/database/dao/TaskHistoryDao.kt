package pl.heng.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pl.heng.database.model.TaskDoneHistory

@Dao
interface TaskHistoryDao {
    @Query("SELECT * FROM TaskDoneHistory ORDER BY creationDate DESC")
    suspend fun getHabitHistoryList() : List<TaskDoneHistory>

    @Query("SELECT COUNT(*) FROM TaskDoneHistory WHERE habitId = :HabitId")
    suspend fun getHabitDoneCount(HabitId : Int) : Int

    @Query("SELECT * FROM TaskDoneHistory WHERE id = :habitDoneId")
    suspend fun getHabitDoneHistory(habitDoneId: Int)  : TaskDoneHistory

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM TaskDoneHistory WHERE habitId = :habitId AND creationDate = :date")
    suspend fun getTodayDone(habitId : Int, date : String) : Int

    @Insert
    suspend fun insertHabitDone(taskDone : TaskDoneHistory)

    @Query("DELETE FROM TaskDoneHistory WHERE id = :id")
    suspend fun deleteHabitDone(id : Int)
}