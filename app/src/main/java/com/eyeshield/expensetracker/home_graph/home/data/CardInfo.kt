package com.eyeshield.expensetracker.home_graph.home.data

import androidx.annotation.ColorRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eyeshield.expensetracker.R
import java.util.UUID

data class CardInfo(
    val id: String = UUID.randomUUID().toString(),
    val position: Int = 0,
    val zIndex: Float = 0f,
    val offsetY: Dp = 0.dp,
    @ColorRes
    val cardColor: Int = R.color.card_color_1
)
