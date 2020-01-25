package pl.heng.viewmodel

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.heng.database.model.Task
import pl.heng.database.repository.Repo
import pl.heng.database.repository.TaskRepository
import java.time.LocalDate
import java.time.LocalTime


class AddTaskViewModel(application: Application) : AndroidViewModel(application), Observable {

    @Bindable
    val taskName = ObservableField<String>()
    @Bindable
    val categoryName = ObservableField<String>()
    @Bindable
    val doesDate = ObservableField<String>().apply {
        set(LocalDate.now().toString())//.format(DateTimeFormatter.ofPattern("dd.mm.YYYY")))
    }
    @Bindable
    val notifyTime = ObservableField<String>().apply {
        set(LocalTime.now().toString())//.format(DateTimeFormatter.ofPattern("HH:MM")))
    }
    private val repository by lazy { Repo(TaskRepository(application)) }

    fun addNewTask() {
        // przyklad Buildera
        val habit =
            Task.Builder()
                .setName(taskName.get() ?: "TestZadania")
                .setCategory(categoryName.get() ?: "TestKategori")
                .setDoesDate(doesDate.get().toString())
                .setRemindHour(notifyTime.get().toString())
                .setCreateDate(LocalDate.now().toString())
                .build()
        viewModelScope.launch {
            //TODO: przyklad DI - oznaczone aby było widoczne
            repository.insert(habit)
        }
    }

    @delegate:Transient
    private val mCallBacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        mCallBacks.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        mCallBacks.add(callback)
    }
}

