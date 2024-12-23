package com.eyeshield.expensetracker.bottomNav

import com.eyeshield.expensetracker.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Tabs(
    val resourceId: Int,
    val icon: Int
) {
    @Serializable
    data object HomeScreen : Tabs(R.string.home, R.drawable.home)

    @Serializable
    data object CalendarScreen :
        Tabs(R.string.calendar, R.drawable.calendar_lines)

    @Serializable
    data object AddScreen : Tabs(R.string.add, R.drawable.add)

    @Serializable
    data object CardScreen :
        Tabs(R.string.credit_card, R.drawable.credit_card)

    @Serializable
    data object SettingsScreen :
        Tabs(R.string.settings, R.drawable.settings)
}
