package com.eyeshield.expensetracker.calendar

interface CustomCalendar {

    val monthName: String?
    val year: Int
    val dayOfWeek: String?

    var monthIndex: Int

    fun getIndexOfWeekForStartDayOfCurrentMonth(): Int
    fun getMonthAndYear(monthFormat: String = "MMMM", yearFormat: String = "yyyy"): String

    fun getCurrentDay(): Int

    /**
     * if month is specified it will return total days for that month
     * else it will return total days for current month
     * **/
    fun getTotalDaysForMonth(): Int
}
