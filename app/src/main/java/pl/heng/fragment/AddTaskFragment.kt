package pl.heng.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.add_task_fragment.*
import pl.heng.R
import pl.heng.databinding.AddTaskFragmentBinding
import pl.heng.viewmodel.AddTaskViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddTaskFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: AddTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding :AddTaskFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.add_task_fragment,null,true)
        viewModel = ViewModelProviders.of(this).
            get(AddTaskViewModel(activity?.application!!)::class.java)
        binding.vm = viewModel
        binding.btnSave.setOnClickListener {
            viewModel.addNewTask()
            this.dismiss()
        }
        return binding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialog.dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setOnShowListener {
                btnNotifyTime.setOnClickListener {
                    notifyHourChange()
                }
                btnEndDate.setOnClickListener {
                    endDateChange()
                }
            }
            window?.setDimAmount(0.2f)
        }
    }

    private fun notifyHourChange(){
        val picker = TimePickerDialog(this.context, R.style.DialogTheme,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                viewModel.notifyTime.set(LocalTime.of(hourOfDay,minute).format(DateTimeFormatter.ofPattern("hh:mm")))
            }, LocalDateTime.now().hour, LocalDateTime.now().minute,true)
        picker.show()
    }

    private fun endDateChange()
    {
        val cldr = Calendar.getInstance()
        val day = cldr.get(Calendar.DAY_OF_MONTH)
        val month = cldr.get(Calendar.MONTH)
        val year = cldr.get(Calendar.YEAR)

        val picker = DatePickerDialog(this.context,R.style.DialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                viewModel.doesDate.set(LocalDate.of(year,monthOfYear+1,dayOfMonth).format(DateTimeFormatter.ofPattern("DD.MM.YYYY")))
            }, year, month, day
        )
        picker.show()
    }

    companion object {
        fun newInstance() = AddTaskFragment()
    }
}
