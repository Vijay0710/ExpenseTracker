package com.eyeshield.expensetracker.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.extensions.horizontalPadding
import com.eyeshield.expensetracker.extensions.topPadding
import com.eyeshield.expensetracker.extensions.verticalPadding

@Composable
fun AnimatedToast(
    shouldShowToast: Boolean,
    errorMessage: String,
    onProgressFinished: () -> Unit,
    backGroundColor: Color = colorResource(R.color.login_surface_background).copy(0.6f),
    borderStroke: BorderStroke? = null,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp)
) {
    AnimatedVisibility(
        visible = shouldShowToast,
        enter = slideInVertically(
            animationSpec = tween(1000),
            initialOffsetY = { -1000 }
        ),
        exit = slideOutVertically(
            animationSpec = tween(1000),
            targetOffsetY = { -1000 }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .topPadding(40.dp)
                .horizontalPadding(20.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                color = backGroundColor,
                shape = shape,
                border = borderStroke
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .verticalPadding(5.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = AlertIcon,
                        contentDescription = "Alert Icon",
                        tint = Color.Unspecified
                    )
                    Text(
                        text = errorMessage,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        ),
                        color = colorResource(R.color.login_screen_error_content),
                        modifier = Modifier
                    )
                }

                AnimatedLinearProgressIndicator(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onAnimationFinished = {
                        onProgressFinished()
                    }
                )
            }
        }
    }
}