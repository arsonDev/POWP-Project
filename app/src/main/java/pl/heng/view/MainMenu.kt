package pl.heng.view

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main_menu.*
import org.jetbrains.anko.contentView
import pl.heng.R
import pl.heng.database.model.Task
import pl.heng.databinding.ActivityMainMenuBinding
import pl.heng.fragment.AddTaskFragment
import pl.heng.fragment.HabitInfoFragment
import pl.heng.fragment.InfoAppFragment
import pl.heng.viewmodel.TasksViewModel
import kotlin.system.exitProcess

class MainMenu : AppCompatActivity() {

    private lateinit var viewModel: TasksViewModel
    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityBinding: ActivityMainMenuBinding =
            DataBindingUtil. setContentView(this, R.layout.activity_main_menu)
        setUI()
        setViewModel(activityBinding)
        configureBackdrop()
    }

    private fun setUI() {
        contentView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        var bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        setSupportActionBar(bottomAppBar)

        fab.setOnClickListener {
            val dialog = AddTaskFragment.newInstance()
            dialog.show(supportFragmentManager, AddTaskFragment::class.java.simpleName)
        }

        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_item -> {
                    val dialog = InfoAppFragment.newInstance()
                    dialog.show(supportFragmentManager, InfoAppFragment::class.java.simpleName)
                    true
                }
                else -> false
            }
        }
    }

    private fun setViewModel(activityBinding: ActivityMainMenuBinding) {
        viewModel = ViewModelProviders.of(this).get(TasksViewModel(application)::class.java)
        activityBinding.vm = viewModel

        viewModel.getHabits().observe(this, Observer<List<Task>> { habits ->
            if (habits.isEmpty())
                viewModel.emptyVisible.set(true)
            else {
                viewModel.emptyVisible.set(false)
                viewModel.setHabitsInAdapter(habits)
            }
        })
        viewModel.selected.observe(this, Observer {
            val dialog = HabitInfoFragment.newInstance()
            dialog.show(supportFragmentManager, HabitInfoFragment::class.java.simpleName)
        })
        viewModel.notifiyMessage.observe(this, Observer{
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        })
    }

    private fun configureBackdrop() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_new)
        fragment?.let {
            BottomSheetBehavior.from(it.view!!)?.let { bsb ->
                bsb.state = BottomSheetBehavior.STATE_HIDDEN
                fab.setOnClickListener {
                    val dialog = AddTaskFragment.newInstance()
                    dialog.show(supportFragmentManager, AddTaskFragment::class.java.simpleName)
                }
                mBottomSheetBehavior = bsb
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }

    override fun onBackPressed() {
        mBottomSheetBehavior?.let {
            if (it.state == BottomSheetBehavior.STATE_EXPANDED) {
                it.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    System.exit(2)
                    exitProcess(-1)
                } else {
                    Toast.makeText(this, "Naciśnij ponownie aby wyjść", Toast.LENGTH_SHORT).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        } ?: if (backPressedTime + 2000 > System.currentTimeMillis()) {
            System.exit(2)
            exitProcess(-1)
        } else {
            Toast.makeText(this, "Naciśnij ponownie aby wyjść", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
