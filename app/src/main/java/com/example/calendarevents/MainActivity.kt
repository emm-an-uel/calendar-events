package com.example.calendarevents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.calendarevents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewmodel
        viewModel = ViewModelProvider(this)[ViewModel::class.java]

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