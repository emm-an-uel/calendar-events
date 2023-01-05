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

class SecondCalendarFragment : Fragment() {
    private var _binding: FragmentSecondCalendarBinding? = null
    private val binding get() = _binding!!

    lateinit var calendarView: CalendarView
    lateinit var daysOfWeek: List<String>
    lateinit var fab: FloatingActionButton
    private var events = listOf<Event2>()

    lateinit var viewModel: ViewModel

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
            setupCalendar()
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

        // add events to calendar
        for (event in events) {
            calendarView.addCalendarObject(
                CalendarView.CalendarObject(
                    null,
                    event.date,
                    ContextCompat.getColor(requireContext(), R.color.teal_700),
                    ContextCompat.getColor(requireContext(), R.color.teal_700)
                )
            )
        }
    }

    private fun syncMonth(currentMonth: Int, currentYear: Int) {
        val month = DateFormatSymbols().months[currentMonth]
        binding.tvMonth.text = "$month $currentYear"
    }
}