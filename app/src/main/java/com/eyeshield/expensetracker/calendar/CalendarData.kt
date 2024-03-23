package com.eyeshield.expensetracker.calendar

data class CalendarData(
    var dayStartingColumn: Int,
    var currentDay: Int,
    var totalDays: Int,
    var selectedDay: Int
)
