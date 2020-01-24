package pl.heng.fragment

import android.database.Observable
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.ViewModel

class HabitInfoViewModel : ViewModel(), androidx.databinding.Observable {

    val loading = ObservableInt(View.VISIBLE)
    val editMode = ObservableBoolean(false)
    val taskName = ObservableField<String>()
    val categoryName = ObservableField<String>()

    @delegate:Transient
    private val mCallBacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    override fun removeOnPropertyChangedCallback(callback: androidx.databinding.Observable.OnPropertyChangedCallback?) {
        mCallBacks.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: androidx.databinding.Observable.OnPropertyChangedCallback?) {
        mCallBacks.add(callback)
    }
}
