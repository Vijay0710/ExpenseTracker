package com.eyeshield.expensetracker.calendar

import java.text.SimpleDateFormat
import java.time.Month
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class CustomCalendarImpl @Inject constructor(
    private val calendar: Calendar
) : CustomCalendar {

    override val monthName: String? =
        calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
    override val year: Int = calendar.get(Calendar.YEAR)
    override val dayOfWeek: String? =
        calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())


    override var monthIndex: Int = Month.valueOf(monthName!!.uppercase()).ordinal

    override fun getIndexOfWeekForStartDayOfCurrentMonth(): Int {
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthIndex)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val dayOfWeekIndex = calendar.get(Calendar.DAY_OF_WEEK)
        // returns the index of weekday of the current month
        // Example if the day is 1st of May, it will return index 5 (Wednesday)
        return dayOfWeekIndex
    }

    override fun getMonthAndYear(monthFormat: String, yearFormat: String): String {
        val monthAndYearFormat = SimpleDateFormat("$monthFormat $yearFormat", Locale.getDefault())
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthIndex)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val monthAndYear = monthAndYearFormat.format(calendar.time)
        // Returns the specified month and year for the given month in their applicable formats
        return monthAndYear
    }

    override fun getCurrentDay(): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    override fun getTotalDaysForMonth(): Int {
        calendar.apply {
            set(Calendar.MONTH, monthIndex)
        }
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}