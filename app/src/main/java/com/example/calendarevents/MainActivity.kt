package com.example.calendarevents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calendarevents.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var fabAddEvent: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fabAddEvent = binding.fabAddEvent
        calendarView = binding.contentMain.calendarView
    }
}