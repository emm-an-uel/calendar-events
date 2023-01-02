package com.example.calendarevents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarevents.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var fabAddEvent: FloatingActionButton
    val events = arrayListOf<Event>()
    val mapOfCalendarDays = mutableMapOf<CalendarDay, Event>() // calendarDay-Event pairs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fabAddEvent = binding.fabAddEvent
        calendarView = binding.contentMain.calendarView

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
            lateinit var calendarDay: CalendarDay

            override fun shouldDecorate(day: CalendarDay?): Boolean {
                return if (mapOfCalendarDays.containsKey(day)) { // check if 'day' is in mapOfCalendarDays (a map of days which have Events)
                    calendarDay = day!!
                    true
                } else false
            }

            override fun decorate(view: DayViewFacade?) {
                val event: Event? = mapOfCalendarDays[calendarDay]
                if (event != null) {
                    view?.addSpan(AddTextToDates(event.name))
                }
            }
        })
    }
}