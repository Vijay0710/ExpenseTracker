package com.eyeshield.expensetracker.home


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.eyeshield.expensetracker.R

enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}


@Composable
fun CreditCard(
    cardFace: CardFace,
    onClick: (CardFace) -> Unit,
    front: @Composable () -> Unit,
    back: @Composable () -> Unit
) {

    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearOutSlowInEasing,
        ), label = "Card Flip Animation"
    )
    

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick(cardFace)
            }
            .graphicsLayer {
                rotationY = rotation.value
            },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(
                id = R.color.grpahite_gray
            )
        ),
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(24.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                if (rotation.value > 90f) rotationY = 180f
            }) {
            Box(modifier = Modifier
                .size(50.dp)
                .align(if (rotation.value <= 90f) Alignment.BottomStart else Alignment.BottomEnd)
                .drawWithContent {
                    drawCircle(radius = 140f, color = Color.White, alpha = 0.1f)
                })
            Box(modifier = Modifier
                .size(70.dp)
                .align(if (rotation.value <= 90f) Alignment.TopEnd else Alignment.TopStart)
                .drawWithContent {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val radius = 150F

                    drawCircle(
                        color = Color.White,
                        center = Offset(centerX, centerY),
                        radius = radius,
                        style = Stroke(width = 70F),
                        alpha = 0.1f,
                    )
                })


            if (rotation.value <= 90f)
                front.invoke()
            else
                back.invoke()
        }
    }
}