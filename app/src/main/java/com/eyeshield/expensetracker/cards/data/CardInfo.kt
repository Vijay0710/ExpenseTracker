package com.eyeshield.expensetracker.cards.data

import androidx.annotation.ColorRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eyeshield.expensetracker.R

data class CardInfo(
    val position: Int = 0,
    val zIndex: Float = 0f,
    val offsetY: Dp = 0.dp,
    @ColorRes
    val cardColor: Int = R.color.card_color_1
)
