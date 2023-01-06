package com.example.calendarevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.calendarevents.databinding.FragmentCalendarDialogBinding
import java.time.temporal.ChronoUnit
import java.util.*

class CalendarDialog : Fragment() {

    private var _binding: FragmentCalendarDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var events: ArrayList<Event2>
    private lateinit var viewModel: ViewModel
    private lateinit var minDate: Calendar
    private lateinit var maxDate: Calendar
    private lateinit var selectedDate: Calendar
    private lateinit var mapOfEvents: MutableMap<Calendar, ArrayList<Event2>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        events = viewModel.getEvents()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setSelectedDate(date: Calendar) {
        selectedDate = date
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMinMaxDates()
        createMapOfEvents()
        pagerAdapter = PagerAdapter(requireContext(), mapOfEvents, minDate, maxDate, selectedDate)
        binding.viewPager.adapter = pagerAdapter
        val index = ChronoUnit.DAYS.between(minDate.toInstant(), selectedDate.toInstant()).toInt()
        // TODO: check if index returns the correct item or if its one off
        binding.viewPager.currentItem = index
    }

    private fun createMapOfEvents() {
        events = viewModel.getEvents()
        mapOfEvents = mutableMapOf()
        val list = arrayListOf<Event2>()
        var key: Calendar = Calendar.getInstance()
        for (event in events) {
            if (!mapOfEvents.containsKey(event.date)) { // if map does not contain a key of that date
                if (list.isNotEmpty()) {
                    mapOfEvents[key] = list // save Calendar-List pair
                }
                list.clear() // resets list
                key = event.date // sets new key
                list.add(event) // adds current event to list which corresponds to above key

            } else { // if map does contain a key of that date, meaning there's >1 event that day
                list.add(event)
            }
        }
    }

    private fun setMinMaxDates() {
        minDate = Calendar.getInstance()
        minDate.set(1992, 0, 1) // note: Calendar has months from 0 - 11
        maxDate = Calendar.getInstance()
        maxDate.set(2100, 0, 1)
    }
}