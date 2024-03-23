package com.eyeshield.expensetracker.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


enum class DayIndexAbbreviation(val index: Int) {
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6)
}

object CalendarUtils {
    private fun getCalendarInstance() : Calendar {
        return Calendar.getInstance()
    }

    fun getStartDayOfTheMonthOffset(month: Int): Int {
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val calendar = getCalendarInstance()
        calendar.set(getCalendarInstance().get(Calendar.YEAR), month, 1)
        val day = dateFormat.format(calendar.time)

        return DayIndexAbbreviation.valueOf(day.uppercase()).index
    }

    fun getMonthAndYear(month: Int) : String {
        val calendar = getCalendarInstance()
        calendar.set(getCalendarInstance().get(Calendar.YEAR), month, 1)
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getCurrentDay(): Int {
        return getCalendarInstance().get(Calendar.DAY_OF_MONTH)
    }

    fun getCurrentMonth() : Int {
        return getCalendarInstance().get(Calendar.MONTH)
    }

    fun getTotalDaysForCurrentMonth(month: Int) : Int {
        val calendar = getCalendarInstance()
        calendar.set(getCalendarInstance().get(Calendar.YEAR),month, 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}