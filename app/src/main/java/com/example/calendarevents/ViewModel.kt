package com.example.calendarevents

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ViewModel(val app: Application): AndroidViewModel(app) {
    private val events = arrayListOf<Event2>()

    @JvmName("getEvents1")
    fun getEvents(): ArrayList<Event2> {
        return events
    }

    fun saveEvent(event: Event2) {
        events.add(event)
    }
}