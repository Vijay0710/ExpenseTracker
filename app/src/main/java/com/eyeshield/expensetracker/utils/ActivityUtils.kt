package com.eyeshield.expensetracker.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.view.WindowInsetsControllerCompat

fun Activity.setStatusBarIconsColorToDark(shouldHaveDarkIcons: Boolean) {
    WindowInsetsControllerCompat(window, window.decorView)
        .isAppearanceLightStatusBars = shouldHaveDarkIcons
}

fun Context.getActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}


fun Context.hasCameraNotch(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val windowInsets = this.getActivity()?.window?.decorView?.rootWindowInsets
        val displayCutout = windowInsets?.displayCutout
        return displayCutout != null
    }
    return false
}