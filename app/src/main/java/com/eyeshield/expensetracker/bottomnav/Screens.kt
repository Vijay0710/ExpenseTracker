package com.eyeshield.expensetracker.bottomnav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eyeshield.expensetracker.R

sealed class Screens(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val icon: Int
) {
    object HomeScreen : Screens("home", R.string.home, R.drawable.home)
    object CalendarScreen : Screens("calendar", R.string.calendar, R.drawable.calendar_lines)
    object AddScreen : Screens("add", R.string.add, R.drawable.add)
    object CardScreen : Screens("card", R.string.credit_card, R.drawable.credit_card)
    object SettingsScreen : Screens("settings", R.string.settings, R.drawable.settings)
}
