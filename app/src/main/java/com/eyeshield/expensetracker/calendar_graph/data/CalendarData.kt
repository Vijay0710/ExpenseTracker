package com.eyeshield.expensetracker.calendar_graph.data

import com.eyeshield.expensetracker.calendar.CustomCalendarImpl

data class CalendarData(
    val monthIndex: Int = 0,
    val monthAndYear: String = "January 1970",
    val dayStartingColumn: Int = 0,
    val currentDay: Int = 1,
    val totalDays: Int = 31,
    val selectedDay: Int = 0
) {
    val isCurrentMonth: Boolean
        get() = CustomCalendarImpl.isCurrentMonth(monthIndex)
}
