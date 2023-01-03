package com.example.calendarevents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calendarevents.databinding.FragmentSecondCalendarBinding

class SecondCalendarFragment : Fragment() {
    private var _binding: FragmentSecondCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }
}