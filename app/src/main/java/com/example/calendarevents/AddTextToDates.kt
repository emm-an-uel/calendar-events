package com.example.calendarevents

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan

class AddTextToDates(text: String): LineBackgroundSpan {

    private val eventName = text

    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lnum: Int
    ) {
        canvas.drawText(eventName, ((left+right)/4).toFloat(), (bottom+15).toFloat(), paint)
    }
}