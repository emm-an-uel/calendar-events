package com.example.calendarevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
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

    // for PagerAdapter swipe animations 
    private val MIN_OFFSET = 0f
    private val MAX_OFFSET = 0.5f
    private val MIN_ALPHA = 0.5f
    private val MIN_SCALE = 0.8f
    private var totalPages = 0

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
        val index = ChronoUnit.DAYS.between(minDate.toInstant(), selectedDate.toInstant()).toInt()
        binding.viewPager.apply {
            offscreenPageLimit = 3
            adapter = pagerAdapter
            currentItem = index
            setPadding(100, 0, 100, 0)
        }
        pagerSwipeAnimations()
    }

    private fun pagerSwipeAnimations() {
        binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // update view scale and alpha
                updatePager(binding.viewPager.findViewWithTag(position), 1f - positionOffset) // current page
                if ((position + 1) < totalPages) { // next page
                    updatePager(binding.viewPager.findViewWithTag(position + 1), positionOffset)
                }
                if ((position + 2) < totalPages) { // two pages in advance
                    // (so it's already made smaller before user can see it - smoother look)
                    updatePager(binding.viewPager.findViewWithTag(position + 2), 0f)
                }
                if ((position - 1) >= 0) { // previous page
                    updatePager(binding.viewPager.findViewWithTag(position - 1), 0f)
                }
                if ((position - 2) >= 0) { // two pages before 
                    updatePager(binding.viewPager.findViewWithTag(position - 2), 0f)
                }
            }

            override fun onPageSelected(position: Int) {
                // do nothing 
            }

            override fun onPageScrollStateChanged(state: Int) {
                // do nothing 
            }

        })
    }

    private fun updatePager(view: View, offset: Float) {
        var adjustedOffset: Float =
            (1.0f - 0.0f) * (offset - MIN_OFFSET) / (MAX_OFFSET - MIN_OFFSET) + 0.0f
        adjustedOffset = if (adjustedOffset > 1f) 1f else adjustedOffset
        adjustedOffset = if (adjustedOffset < 0f) 0f else adjustedOffset

        val alpha: Float =
            adjustedOffset * (1f - MIN_ALPHA) + MIN_ALPHA
        val scale: Float =
            adjustedOffset * (1f - MIN_SCALE) + MIN_SCALE

        view.alpha = alpha
        view.scaleY = scale
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
        totalPages = ChronoUnit.DAYS.between(minDate.toInstant(), maxDate.toInstant()).toInt()
    }
}