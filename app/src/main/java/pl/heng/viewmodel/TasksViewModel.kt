package pl.heng.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.heng.R
import pl.heng.adapter.TaskListAdapter
import pl.heng.database.model.Task
import pl.heng.database.model.TaskDoneHistory
import pl.heng.database.repository.TaskDoneRepository
import pl.heng.database.repository.TaskRepository
import java.time.LocalDate

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepo = TaskRepository(application)
    private val taskDoneRepository = TaskDoneRepository(application)
    private val adapter = TaskListAdapter(R.layout.habit_item, this)
    val selected = MutableLiveData<Task>()
    val emptyVisible = ObservableBoolean(true)
    val notifiyMessage = MutableLiveData<String>("")

    fun getTasks() = taskRepo.getHabitList()

    fun getAdapter() = adapter

    fun getTaskAt(position: Int) = adapter.getHabit(position)

    fun setHabitsInAdapter(breeds: List<Task>) {
        this.adapter.setHabits(breeds)
        this.adapter.notifyDataSetChanged()
    }

    fun onItemClick(position: Int) {
        selected.value = getTaskAt(position)
    }

    fun onItemClickDone(position: Int) {
        val task = getTaskAt(position)
        viewModelScope.launch {
            val done = taskDoneRepository.getHabitDoneToday(task.uid, LocalDate.now())
            if (!done) {
                val habitDone = TaskDoneHistory(task.uid,true,LocalDate.now().toString())
                task.buttonText = "wykonano"

                taskDoneRepository.insert(habitDone)
                notifiyMessage.value = "Brawo za wykonanie zadania : ${task.name} #${task.category}"
            }else{
                notifiyMessage.value = "Dzisiaj wykonano już zadanie : ${task.name} #${task.category}"
                task.buttonText = "wykonaj"
            }
            task.done = done
        }
    }
}