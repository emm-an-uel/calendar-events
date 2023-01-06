package com.example.calendarevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.calendarevents.databinding.FragmentSecondCalendarBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.hugoandrade.calendarviewlib.CalendarView
import java.text.DateFormatSymbols
import java.util.*

class SecondCalendarFragment : Fragment() {
    private var _binding: FragmentSecondCalendarBinding? = null
    private val binding get() = _binding!!

    lateinit var calendarView: CalendarView
    lateinit var daysOfWeek: List<String>
    lateinit var fab: FloatingActionButton
    private var events = listOf<Event2>()

    lateinit var viewModel: ViewModel

    private lateinit var calendarDialog: CalendarDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events = viewModel.getEvents()

        calendarView = binding.calendarView
        daysOfWeek = resources.getStringArray(R.array.days_of_week).toList()
        setupCalendar()

        fab = binding.fabAddEvent
        fab.setOnClickListener {
            createNewEvent()
        }

        // listen for new events
        childFragmentManager.setFragmentResultListener("newEvent", this) { _, _ ->
            events = viewModel.getEvents()
            addEvents()
        }
    }

    private fun createNewEvent() {
        val dialog = NewEventDialog()
        dialog.show(childFragmentManager, "dialog")
    }

    private fun setupCalendar() {
        // initial sync
        val currentMonth = calendarView.shownMonth
        val currentYear = calendarView.shownYear
        syncMonth(currentMonth, currentYear)

        // sync as user swipes through calendar
        calendarView.setOnMonthChangedListener { month, year ->
            syncMonth(month, year)
        }

        addEvents() // add user events to calendar
        calendarView.setOnItemClickedListener { calendarObjects, previousDate, selectedDate ->
            if (calendarObjects.size > 0) { // if there are events
                showCalendarDialog(selectedDate)

            } else { // if no events that day
                createNewEvent()
            }
        }
    }

    private fun showCalendarDialog(selectedDate: Calendar) {
        calendarDialog = CalendarDialog()
        calendarDialog.setSelectedDate(selectedDate)
        binding.frameLayout.visibility = View.VISIBLE
        childFragmentManager.beginTransaction().replace(binding.frameLayout.id, calendarDialog).commit()
    }

    private fun addEvents() {
        val calObjectList = arrayListOf<CalendarView.CalendarObject>() // resets list to prevent duplicate Events
        // previously, I looped through each event in events as below and added a CalendarObject each
        // this caused duplicate events when the fragment is resumed since the CalendarObjects added before were not removed
        for (event in events) {
            calObjectList.add(
                CalendarView.CalendarObject(
                    null,
                    event.date, // where 'date': Calendar
                    ContextCompat.getColor(requireContext(), R.color.teal_700),
                    ContextCompat.getColor(requireContext(), R.color.teal_700)
                ))
        }
        calendarView.setCalendarObjectList(calObjectList)
    }

    private fun syncMonth(currentMonth: Int, currentYear: Int) {
        val month = DateFormatSymbols().months[currentMonth]
        binding.tvMonth.text = "$month $currentYear"
    }
}