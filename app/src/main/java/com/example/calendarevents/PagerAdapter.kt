package com.example.calendarevents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.temporal.ChronoUnit
import java.util.*

class PagerAdapter(
    private val context: Context,
    private val mapOfEvents: Map<Calendar, List<Event2>>,
    private val minDate: Calendar,
    private val maxDate: Calendar,
    private val selectedDate: Calendar
) : PagerAdapter() {

    private val initialPosition = ChronoUnit.DAYS.between(minDate.toInstant(), selectedDate.toInstant()).toInt()
    // number of days between minDate and selectedDate to determine ViewPager's initial position

    private val initialPageAndDate = Pair<Int, Calendar>(initialPosition, selectedDate)

    override fun getCount(): Int {
        return ChronoUnit.DAYS.between(minDate.toInstant(), maxDate.toInstant()).toInt() // total number of days between minDate and maxDate
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val currentDate: Calendar = initialPageAndDate.second.clone() as Calendar
        currentDate.add(Calendar.DATE, position - initialPageAndDate.first) // adds the number of days it is away from the initialDate

        val view = LayoutInflater.from(context).inflate(R.layout.calendar_card_item, container, false)
        view.tag = position // tag for adjustments of size and opacity in CalendarDialog.updatePager

        val tvDayOfMonth: TextView = view.findViewById(R.id.tvDayOfMonth)
        val tvDayOfWeek: TextView = view.findViewById(R.id.tvDayOfWeek)
        val tvNoEvents: TextView = view.findViewById(R.id.tvNoEvents)
        val rvEvents: RecyclerView = view.findViewById(R.id.rvEvents)
        val fabAddEvent: FloatingActionButton = view.findViewById(R.id.fabAddEvent)

        tvDayOfMonth.text = currentDate.get(Calendar.DAY_OF_MONTH).toString()
        tvDayOfWeek.text = getDayOfWeek(currentDate.get(Calendar.DAY_OF_WEEK))
        fabAddEvent.setOnClickListener {
            Toast.makeText(context, "Add new event", Toast.LENGTH_SHORT).show()
        }

        // show today's events
        var hasEvents = false
        for (key in mapOfEvents.keys) { // check if mapOfEvents contains a key with same date as currentDate
            if (isSameDate(key, currentDate)) {
                val todayEvents: List<Event2> = mapOfEvents[key]!!
                val adapter = RecyclerViewAdapter(todayEvents)
                rvEvents.adapter = adapter
                hasEvents = true
                break
            }
        }

        if (!hasEvents) { // no events for the day
            rvEvents.visibility = View.GONE
            tvNoEvents.visibility = View.VISIBLE
        }

        container.addView(view)
        return view
    }

    private fun isSameDate(date1: Calendar, date2: Calendar): Boolean {
        if (date1.get(Calendar.DAY_OF_MONTH) != date2.get(Calendar.DAY_OF_MONTH)) {
            return false
        }
        if (date1.get(Calendar.MONTH) != date2.get(Calendar.MONTH)) {
            return false
        }
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
    }

    private fun getDayOfWeek(dayInt: Int): String {
        return when (dayInt) {
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> "Sunday"
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE // not sure what this does but it was included in the original CalendarView-Widget app by hugomfandrade
    }
}