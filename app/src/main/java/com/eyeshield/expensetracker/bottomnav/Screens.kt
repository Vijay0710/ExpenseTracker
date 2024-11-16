package com.eyeshield.expensetracker.bottomnav

import com.eyeshield.expensetracker.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Screens(
    val resourceId: Int,
    val icon: Int
) {
    @Serializable
    data object HomeScreen : Screens(R.string.home, R.drawable.home)

    @Serializable
    data object CalendarScreen :
        Screens(R.string.calendar, R.drawable.calendar_lines)

    @Serializable
    data object AddScreen : Screens(R.string.add, R.drawable.add)

    @Serializable
    data object CardScreen :
        Screens(R.string.credit_card, R.drawable.credit_card)

    @Serializable
    data object SettingsScreen :
        Screens(R.string.settings, R.drawable.settings)
}
