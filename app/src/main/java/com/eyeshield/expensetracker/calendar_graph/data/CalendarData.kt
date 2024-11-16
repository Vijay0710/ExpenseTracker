package com.eyeshield.expensetracker.calendar_graph.data

data class CalendarData(
    var currentMonthPosition: Int,
    var dayStartingColumn: Int,
    var currentDay: Int,
    var totalDays: Int,
    var selectedDay: Int
)
