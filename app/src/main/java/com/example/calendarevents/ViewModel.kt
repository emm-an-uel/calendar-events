package com.example.calendarevents

import android.app.Application
import android.util.Log
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
        val list: ArrayList<Event2> = arrayListOf()
        var key: Calendar? = null
        for (event in events) {
            if (key != null) { // not the first item in list
                if (isSameDate(key, event.date)) { // this event is on the same date as the other events in list
                    list.add(event)

                } else { // this event is on a new date
                    mapOfEvents[key] = list // add list of events before this new event
                    key = event.date // set new key
                    list.clear() // reset list
                    list.add(event) // add new event to new list


                    // DEBUGGING - checking the first item in map
                    val k1: Calendar = events[0].date
                    val l1: List<Event2> = mapOfEvents[k1]!!
                    Log.e(k1.get(Calendar.DAY_OF_MONTH).toString(), l1.size.toString())
                    Log.e("", "")

                }

            } else { // first item in list
                key = event.date
                list.add(event)
            }
        }
        if (key != null && list.isNotEmpty()) {
            mapOfEvents[key] = list // save last-added <Calendar, List> pair
        }

        // DEBUGGING
        // TODO: fix - event on the latest date overwrites every other event
        for ((key1, list1) in mapOfEvents) {
            Log.e("Key", key1.get(Calendar.DAY_OF_MONTH).toString())
            Log.e("List Size", list1.size.toString())
            for (event1 in list1) {
                Log.e("Event Name", event1.name)
            }
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