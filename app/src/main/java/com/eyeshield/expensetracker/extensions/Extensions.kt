package com.eyeshield.expensetracker.extensions

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

fun Modifier.topPadding(top: Dp): Modifier = padding(top = top)
fun Modifier.bottomPadding(bottom: Dp): Modifier = padding(bottom = bottom)
fun Modifier.startPadding(start: Dp): Modifier = padding(start = start)
fun Modifier.endPadding(end: Dp): Modifier = padding(end = end)
fun Modifier.horizontalPadding(horizontal: Dp) = padding(start = horizontal, end = horizontal)
fun Modifier.verticalPadding(vertical: Dp) = padding(top = vertical, bottom = vertical)