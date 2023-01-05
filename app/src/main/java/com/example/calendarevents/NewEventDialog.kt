package com.example.calendarevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.calendarevents.databinding.NewEventDialogBinding
import java.util.*

class NewEventDialog: DialogFragment() {
    private var _binding: NewEventDialogBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ViewModel
    lateinit var date: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NewEventDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date = Calendar.getInstance() // default date is today's date
        binding.cv.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date.set(year, month, dayOfMonth) // updates date according to user changes
        }

        binding.btnSave.setOnClickListener {
            saveEvent()
            parentFragmentManager.setFragmentResult("newEvent", bundleOf())
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun saveEvent() {
        val name = if (binding.etTitle.text != null) {
            binding.etTitle.text.toString()
        } else {
            "New Event"
        }
        
        val newEvent = Event2(name, date)
        viewModel.saveEvent(newEvent)
    }
}