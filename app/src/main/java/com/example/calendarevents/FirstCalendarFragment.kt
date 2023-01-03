package com.example.calendarevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calendarevents.databinding.FragmentFirstCalendarBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.time.LocalDate

class FirstCalendarFragment : Fragment() {
    private var _binding: FragmentFirstCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var fabAddEvent: FloatingActionButton
    val events = arrayListOf<Event>()
    val mapOfCalendarDays = mutableMapOf<CalendarDay, Event>() // calendarDay-Event pairs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabAddEvent = binding.fabAddEvent
        calendarView = binding.calendarView

        createDummyEvents()
        convertToCalendarDays(events)
        setupCalendar()
    }

    private fun createDummyEvents() {
        for (n in 0 until 3) {
            val eventName = "Event $n"
            when (n) {
                0 -> {
                    val date = LocalDate.now()
                    events.add(Event(eventName, date))
                }
                1 -> {
                    val date = LocalDate.now()
                    val newDate = date.plusDays(1)
                    events.add(Event(eventName, newDate))
                }
                else -> {
                    val date = LocalDate.now()
                    val newDate = date.plusMonths(1)
                    events.add(Event(eventName, newDate))
                }
            }
        }
    }

    private fun convertToCalendarDays(events: List<Event>) {
        // add each event to a list of calendarDays which will be checked against when decorating CalendarView
        for (event in events) {
            val localDate: LocalDate = event.date
            val year: Int = localDate.year
            val month: Int = localDate.monthValue
            val day: Int = localDate.dayOfMonth
            val calendarDay: CalendarDay = CalendarDay.from(year, month, day)

            mapOfCalendarDays[calendarDay] = event // pairs calendarDay with the corresponding event
        }
    }

    private fun setupCalendar() {
        calendarView.addDecorator(object: DayViewDecorator {

            override fun shouldDecorate(day: CalendarDay?): Boolean {
                return mapOfCalendarDays.containsKey(day)
            }

            override fun decorate(view: DayViewFacade?) {
                view?.addSpan(DotSpan(5F, R.color.teal_200))
                //if (view != null && mapOfCalendarDays[calendarDay] != null) {
                //    addEvent(view, mapOfCalendarDays[calendarDay]!!)
                //}
            }

            private fun addEvent(view: DayViewFacade, eventName: String) {
                view.addSpan(AddTextToDates(eventName))
            }
        })
    }
}