package com.eyeshield.expensetracker.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eyeshield.expensetracker.R

@Composable
@Preview
fun GradientBackground(
    content: @Composable () -> Unit = {}
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidthPx = configuration.screenWidthDp

    val smallDimension = minOf(
        configuration.screenWidthDp.dp,
        configuration.screenHeightDp.dp
    )

    val smallDimensionPx = with(density) {
        smallDimension.roundToPx()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.login_screen_background))
    ) {
        Box(
            modifier = Modifier
                .blur(smallDimension / 3.6f, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colorResource(R.color.login_screen_blur_circle_color_1),
                            colorResource(R.color.login_screen_blur_circle_color_2)
                        ),
                        center = Offset(
                            x = screenWidthPx / 2f,
                            y = -100f
                        ),
                        radius = smallDimensionPx / 2f
                    ),
                )
                .size(200.dp)
                .align(Alignment.TopCenter)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}