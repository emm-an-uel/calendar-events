package com.example.calendarevents

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.util.*

class ViewModel(val app: Application): AndroidViewModel(app) {
    private val events = arrayListOf<Event2>()
    private val mapOfEvents = mutableMapOf<Calendar, List<Event2>>()

    @JvmName("getEvents1")
    fun getEvents(): ArrayList<Event2> {
        return events
    }

    fun saveEvent(event: Event2) {
        events.add(event)
        events.sortBy { it.date }
    }

    fun getMapOfEvents(): Map<Calendar, List<Event2>> {
        return mapOfEvents
    }

    fun createMapOfEvents() {
        var list: ArrayList<Event2> = arrayListOf()
        var key: Calendar? = null
        for (event in events) {
            if (key != null) { // not the first item in list
                if (isSameDate(key, event.date)) { // this event is on the same date as the other events in list
                    list.add(event)

                } else { // this event is on a new date
                    mapOfEvents[key] = list // add list of events before this new event
                    key = event.date // set new key
                    list = arrayListOf() // reset list
                    list.add(event) // add new event to new list
                }

            } else { // first item in list
                key = event.date
                list.add(event)
            }
        }
        if (key != null && list.isNotEmpty()) {
            mapOfEvents[key] = list // save last-added <Calendar, List> pair
        }
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
}