package pl.heng.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.heng.BR
import pl.heng.database.model.Task
import pl.heng.viewmodel.TasksViewModel


class TaskListAdapter(layoutId : Int, viewModel : TasksViewModel) : RecyclerView.Adapter<HabitViewHolder>() {

    private var _tasks : List<Task> = ArrayList()
    private val _layoutId = layoutId
    private val _viewModel = viewModel

    override fun getItemCount(): Int {
        return _tasks.size
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(_viewModel,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val layoutInflanter = LayoutInflater.from(parent.context)
        val viewDataBinding : ViewDataBinding = DataBindingUtil.inflate(layoutInflanter,viewType,parent,false)
        return HabitViewHolder(viewDataBinding)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    private fun getLayoutIdForPosition(position: Int) = _layoutId

    fun setHabits(tasks : List<Task>){
        this._tasks = tasks
        notifyDataSetChanged()
    }

    fun getHabit(position: Int) = _tasks.get(position)
}

class HabitViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel : TasksViewModel, position: Int){
        viewModel.getTaskAt(position)
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.position, position)
        binding.executePendingBindings()
    }
}