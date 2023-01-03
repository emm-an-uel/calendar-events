package com.example.calendarevents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calendarevents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // buttons
        binding.buttonFirst.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(binding.contentMain.frameLayout.id, FirstCalendarFragment()).commit()
        }
        binding.buttonSecond.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(binding.contentMain.frameLayout.id, SecondCalendarFragment()).commit()
        }
    }
}