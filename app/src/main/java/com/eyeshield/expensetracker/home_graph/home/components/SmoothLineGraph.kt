package com.eyeshield.expensetracker.home_graph.home.components

import android.graphics.PointF
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntRect
import androidx.compose.ui.unit.toSize
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
@Preview(showBackground = true)
fun SmoothLineGraph() {
    Box(
        modifier = Modifier.padding(top = 20.dp)
    ) {
        val isPreview = LocalInspectionMode.current
        val animationProgress = remember {
            Animatable(
                if (isPreview) 1f else 0f
            )
        }
        var highlightedWeek by remember { mutableStateOf<Int?>(null) }
        val localView = LocalView.current

        LaunchedEffect(highlightedWeek) {
            if (highlightedWeek != null) {
                localView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            }
        }

        LaunchedEffect(graphData) {
            animationProgress.animateTo(1f, tween(3000))
        }

        val textMeasurer = rememberTextMeasurer()
        val labelTextStyle = MaterialTheme.typography.labelSmall

        Spacer(
            modifier = Modifier
                .padding(8.dp)
                .aspectRatio(3 / 2f)
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { offset ->
                            highlightedWeek =
                                (offset.x / (size.width / (graphData.size - 1))).roundToInt()
                        },
                        onDragEnd = { highlightedWeek = null },
                        onDragCancel = { highlightedWeek = null },
                        onDrag = { change, _ ->
                            highlightedWeek =
                                (change.position.x / (size.width / (graphData.size - 1))).roundToInt()
                        }
                    )
                }
                .drawWithCache {
                    val path = getSmoothPath(graphData, size)
                    val filledPath = Path()
                    filledPath.addPath(path)
                    filledPath.relativeLineTo(0f, size.height)
                    filledPath.lineTo(0f, size.height)
                    filledPath.close()
                    onDrawBehind {
                        clipRect(right = size.width * animationProgress.value) {
                            drawPath(path, Color(0xFF1c2024), style = Stroke(2.dp.toPx()))

                            drawPath(
                                filledPath,
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF1c2024).copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                ),
                                style = Fill
                            )
                        }

                        // draw highlight if user is dragging
                        highlightedWeek?.let {
                            drawHighlight(it, graphData, textMeasurer, labelTextStyle)
                        }
                    }
                }
        )
    }
}

fun getSmoothPath(data: List<Balance>, size: Size): Path {
    val path = Path()
    val numberOfEntries = data.size - 1
    val weekWidth = size.width / numberOfEntries
    val max = data.maxBy {
        it.amount
    }
    val min = data.minBy {
        it.amount
    }

    val range = max.amount - min.amount
    val heightPxPerAmount = size.height / range.toFloat()

    var previousBalanceX = 0f
    var previousBalanceY = size.height

    data.forEachIndexed { i, balance ->
        if (i == 0) {
            path.moveTo(
                0f,
                size.height - (balance.amount - min.amount).toFloat() *
                        heightPxPerAmount
            )
        }
        val balanceX = i * weekWidth
        val balanceY = size.height - (balance.amount - min.amount).toFloat() *
                heightPxPerAmount

        val controlPoint1 = PointF((balanceX + previousBalanceX) / 2f, previousBalanceY)
        val controlPoint2 = PointF((balanceX + previousBalanceX) / 2f, balanceY)
        path.cubicTo(
            controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y,
            balanceX, balanceY
        )

        previousBalanceX = balanceX
        previousBalanceY = balanceY
    }
    return path
}

fun DrawScope.drawHighlight(
    highlightedWeek: Int,
    graphData: List<Balance>,
    textMeasurer: TextMeasurer,
    labelTextStyle: TextStyle
) {
    val reformedHighlightedWeek =
        if (highlightedWeek <= -1) 0
        else if (highlightedWeek >= graphData.size) graphData.size - 1
        else highlightedWeek

    // When we force drag to the end or start index becomes OutOfBounds So we need to handle it manually
    val amount = graphData[reformedHighlightedWeek].amount
    val minAmount = graphData.minBy { it.amount }.amount
    val range = graphData.maxBy { it.amount }.amount - minAmount
    val percentageHeight = ((amount - minAmount).toFloat() / range.toFloat())
    val pointY = size.height - (size.height * percentageHeight)
    // draw vertical line on week
    val x = reformedHighlightedWeek * (size.width / (graphData.size - 1))
    drawLine(
        HighlightColor,
        start = Offset(x, pointY),
        end = Offset(x, size.height),
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f))
    )

    // draw hit circle on graph
    drawCircle(
        Color(0xFF1c2024),
        radius = 8.dp.toPx(),
        style = Stroke(width = 14f),
        center = Offset(x, pointY),
    )

    // Filling the circle with white color
    drawCircle(
        Color.White,
        radius = 8.dp.toPx(),
        style = Fill,
        center = Offset(x, pointY),
    )

    // draw info box
    val textLayoutResult = textMeasurer.measure("$amount", style = labelTextStyle)
    val highlightContainerSize = (textLayoutResult.size).toIntRect().inflate(4.dp.roundToPx()).size
    val boxTopLeft = (x - (highlightContainerSize.width / 2f))
        .coerceIn(0f, size.width - highlightContainerSize.width)

    drawRoundRect(
        Color.Gray.copy(0.1f),
        topLeft = Offset(boxTopLeft - 1.dp.toPx(), pointY - 41.dp.toPx()),
        size = Size(
            highlightContainerSize.width.toFloat() + 6f,
            highlightContainerSize.height.toFloat() + 6f
        ),
        cornerRadius = CornerRadius(8.dp.toPx()),
    )

    drawRoundRect(
        Color.White,
        topLeft = Offset(boxTopLeft, pointY - 40.dp.toPx()),
        size = highlightContainerSize.toSize(),
        cornerRadius = CornerRadius(8.dp.toPx()),
    )

    drawText(
        textLayoutResult,
        color = Color.Black,
        topLeft = Offset(boxTopLeft + 4.dp.toPx(), pointY + 4.dp.toPx() - 40.dp.toPx())
    )
}


val HighlightColor = Color.Black.copy(alpha = 0.3f)

val graphData = listOf(
    Balance(LocalDate.now(), BigDecimal(65631)),
    Balance(LocalDate.now().plusWeeks(1), BigDecimal(65931)),
    Balance(LocalDate.now().plusWeeks(2), BigDecimal(65851)),
    Balance(LocalDate.now().plusWeeks(3), BigDecimal(65931)),
    Balance(LocalDate.now().plusWeeks(4), BigDecimal(66484)),
    Balance(LocalDate.now().plusWeeks(5), BigDecimal(67684)),
    Balance(LocalDate.now().plusWeeks(6), BigDecimal(66684)),
    Balance(LocalDate.now().plusWeeks(7), BigDecimal(66984)),
    Balance(LocalDate.now().plusWeeks(8), BigDecimal(70600)),
    Balance(LocalDate.now().plusWeeks(9), BigDecimal(71600)),
    Balance(LocalDate.now().plusWeeks(10), BigDecimal(72600)),
    Balance(LocalDate.now().plusWeeks(11), BigDecimal(72526)),
    Balance(LocalDate.now().plusWeeks(12), BigDecimal(72976)),
    Balance(LocalDate.now().plusWeeks(13), BigDecimal(73589))
)

data class Balance(val date: LocalDate, val amount: BigDecimal)