package pl.heng.database.model

import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskDoneHistory(
    @ColumnInfo(name = "habitId") val habitId : Int,
    @ColumnInfo(name = "done") val done : Boolean,
    @ColumnInfo(name = "creationDate") val date : String
) :  BaseObservable(), IModel {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}