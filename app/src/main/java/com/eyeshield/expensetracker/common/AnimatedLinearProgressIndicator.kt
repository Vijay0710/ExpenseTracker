package com.eyeshield.expensetracker.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eyeshield.expensetracker.R


// Note: In preview you may not be able to view animation as desired so you need to update the initial value and target value in the preview
@Composable
@Preview
fun AnimatedLinearProgressIndicator(
    modifier: Modifier = Modifier,
    indicatorProgress: Float = 0f,
    duration: Int = 7000,
    color: Color = colorResource(R.color.white),
    trackColor: Color = Color.Transparent,
    onAnimationFinished: ((Float) -> Unit)? = null
) {
    var progress by remember { mutableFloatStateOf(1f) }

    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing),
        label = ""
    )

    LaunchedEffect(progressAnimation) {
        progress = indicatorProgress
        // There is some issue in onAnimationFinishedListener which leads to delay so manually invoking it
        if (progressAnimation <= 0.05f) {
            onAnimationFinished?.invoke(0f)
        }
    }

    Box(modifier = modifier) {
        LinearProgressIndicator(
            progress = { progressAnimation },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            color = color,
            trackColor = trackColor
        )
    }

}