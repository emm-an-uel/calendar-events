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
    private val mapOfEvents: MutableMap<Calendar, ArrayList<Event2>>,
    private val minDate: Calendar,
    private val maxDate: Calendar,
    private val selectedDate: Calendar
) : PagerAdapter() {

    private val initialPosition = ChronoUnit.DAYS.between(minDate.toInstant(), selectedDate.toInstant()).toInt()
    private val initialPageAndDate = Pair<Int, Calendar>(initialPosition, selectedDate)

    override fun getCount(): Int {
        return ChronoUnit.DAYS.between(minDate.toInstant(), maxDate.toInstant()).toInt()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val currentDate: Calendar = initialPageAndDate.second.clone() as Calendar
        currentDate.add(Calendar.DATE, position - initialPageAndDate.first)

        val view = LayoutInflater.from(context).inflate(R.layout.calendar_card_item, container, false)
        view.tag = position

        val tvDayOfMonth: TextView = view.findViewById(R.id.tvDayOfMonth)
        val tvDayOfWeek: TextView = view.findViewById(R.id.tvDayOfWeek)
        val tvNoEvents: TextView = view.findViewById(R.id.tvNoEvents)
        val rvEvents: RecyclerView = view.findViewById(R.id.rvEvents)
        val fabAddEvent: FloatingActionButton = view.findViewById(R.id.fabAddEvent)

        tvDayOfMonth.text = currentDate.get(Calendar.DAY_OF_MONTH).toString()
        tvDayOfWeek.text = currentDate.get(Calendar.DAY_OF_WEEK).toString()
        fabAddEvent.setOnClickListener {
            Toast.makeText(context, "Add new event", Toast.LENGTH_SHORT).show()
        }

        if (mapOfEvents.containsKey(currentDate)) { // if there are events that day
            val adapter = RecyclerViewAdapter(mapOfEvents[currentDate]!!)
            rvEvents.adapter = adapter
        } else {
            rvEvents.visibility = View.GONE
            tvNoEvents.visibility = View.VISIBLE
        }

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}