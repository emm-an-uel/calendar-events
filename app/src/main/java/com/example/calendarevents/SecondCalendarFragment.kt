package com.example.calendarevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = binding.calendarView
        daysOfWeek = resources.getStringArray(R.array.days_of_week).toList()
        setupCalendar()

        fab = binding.fabAddEvent
        fab.setOnClickListener {
            createNewEvent()
        }
    }

    private fun createNewEvent() {

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
    }

    private fun syncMonth(currentMonth: Int, currentYear: Int) {
        val month = DateFormatSymbols().months[currentMonth]
        binding.tvMonth.text = "$month $currentYear"
    }
}