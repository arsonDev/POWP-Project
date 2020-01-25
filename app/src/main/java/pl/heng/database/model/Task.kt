package pl.heng.database.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Task(
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "doesDate") val doesDate: String,
    @ColumnInfo(name = "remindHour") val remindHour: String,
    @ColumnInfo(name = "createDate") val createDate: String
) : BaseObservable(), IModel {

    @PrimaryKey(autoGenerate = true) var uid : Int = 0
    @Transient
    var done = false
    @Transient
    @Bindable
    var buttonText = "wykonaj"

    private constructor(builder: Builder) : this(builder.name, builder.category,builder.doesDate,builder.remindHour,builder.createDate)

    class Builder {
        var name: String = "Zadanie"
            private set
        var category: String = "Kategoria"
            private set
        var doesDate: String = ""
            private set
        var remindHour: String = ""
            private set
        var createDate: String = ""
            private set

        fun setName(name: String) = apply { this.name = name }
        fun setCategory(category: String) = apply { this.category = category }
        fun setDoesDate(doesDate: String) = apply { this.doesDate =  doesDate}
        fun setRemindHour(remindHour: String) = apply { this.remindHour = remindHour }
        fun setCreateDate(createDate: String) = apply { this.createDate = createDate }
        fun build() = Task(this)
    }
}