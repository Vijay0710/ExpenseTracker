package com.eyeshield.expensetracker.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.extensions.topPadding
import com.eyeshield.expensetracker.utils.hasCameraNotch

@Composable
fun NetworkConnectivity(
    modifier: Modifier = Modifier,
    isOffline: Boolean,
    shouldShowNetworkStatusIndicator: Boolean
) {

    val context = LocalContext.current
    val networkBackgroundColor = animateColorAsState(
        targetValue = if (isOffline)
            colorResource(R.color.network_lost_background)
        else
            colorResource(R.color.network_available_background),
        label = "Network Background Color Animation",
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )

    val hasCameraNotchInDisplay = remember { context.hasCameraNotch() }

    Column(
        modifier = modifier
            .topPadding(20.dp + if (hasCameraNotchInDisplay) 15.dp else 0.dp)
            .fillMaxWidth()
    ) {
        AnimatedVisibility(visible = shouldShowNetworkStatusIndicator) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(color = networkBackgroundColor.value)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (isOffline) "Offline" else "Online",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.white)
                    ),
                )
            }
        }
    }
}