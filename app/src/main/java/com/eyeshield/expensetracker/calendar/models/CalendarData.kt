package com.eyeshield.expensetracker.calendar.models

data class CalendarData(
    var currentMonthPosition: Int,
    var dayStartingColumn: Int,
    var currentDay: Int,
    var totalDays: Int,
    var selectedDay: Int
)
