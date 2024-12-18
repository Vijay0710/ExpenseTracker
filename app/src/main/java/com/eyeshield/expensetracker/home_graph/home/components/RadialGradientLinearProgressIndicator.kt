package com.eyeshield.expensetracker.home_graph.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.eyeshield.expensetracker.R
import kotlinx.coroutines.delay

@Composable
fun RowScope.RadialGradientLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    startColor: Color,
    endColor: Color
) {
    val progressState = remember { Animatable(0f) }
    LaunchedEffect(progress) {
        delay(500)
        progressState.animateTo(
            progress,
            animationSpec = tween(500, easing = LinearOutSlowInEasing)
        )
    }

    Canvas(
        modifier = modifier
            .weight(1f)
            .height(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.linear_progress_background))
    ) {
        val gradientBrush = Brush.radialGradient(
            colors = listOf(startColor, endColor),
            center = Offset(size.width / 2, size.height / 2),
            radius = size.width / 2
        )
        drawRect(
            brush = gradientBrush,
            alpha = 1f,
            topLeft = Offset.Zero,
            size = Size(size.width * progressState.value, size.height),
            style = Stroke(width = size.height),
        )
    }
}