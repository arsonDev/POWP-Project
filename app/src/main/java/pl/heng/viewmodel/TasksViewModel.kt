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

    private val habitRepo = TaskRepository(application)
    private val habitDoneRepository = TaskDoneRepository(application)
    private val adapter = TaskListAdapter(R.layout.habit_item, this)
    val selected = MutableLiveData<Task>()
    val emptyVisible = ObservableBoolean(true)
    val notifiyMessage = MutableLiveData<String>("")

    fun getHabits() = habitRepo.getHabitList()

    fun getAdapter() = adapter

    fun getHabitAt(position: Int) = adapter.getHabit(position)

    fun setHabitsInAdapter(breeds: List<Task>) {
        this.adapter.setHabits(breeds)
        this.adapter.notifyDataSetChanged()
    }

    fun onItemClick(position: Int) {
        selected.value = getHabitAt(position)
    }

    fun onItemClickDone(position: Int) {
        val habit = getHabitAt(position)
        viewModelScope.launch {
            val done = habitDoneRepository.getHabitDoneToday(habit.uid, LocalDate.now())
            if (!done) {
                val habitDone = TaskDoneHistory(habit.uid,true,LocalDate.now().toString())
                habit.buttonText = "wykonano"

                habitDoneRepository.insert(habitDone)
                notifiyMessage.value = "Brawo za wykonanie zadania : ${habit.name} #${habit.category}"
            }else{
                notifiyMessage.value = "Dzisiaj wykonano już zadanie : ${habit.name} #${habit.category}"
                habit.buttonText = "wykonaj"
            }
            habit.done = done
        }
    }
}