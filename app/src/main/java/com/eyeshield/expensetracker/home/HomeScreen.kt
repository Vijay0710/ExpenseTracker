package com.eyeshield.expensetracker.home


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import kotlinx.coroutines.delay

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF6F6F6)
fun HomeScreen() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    var cardFace by remember { mutableStateOf(CardFace.Front) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    "Good Morning!", style = TextStyle(
                        fontSize = 15.sp, color = colorResource(id = R.color.greeting),
                        fontFamily = FontFamily(Font(R.font.nunito_regular))
                    )
                )

                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Vijay A", style = TextStyle(
                        fontSize = 20.sp, color = colorResource(id = R.color.username),
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    )
                )
            }

            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically)
                    .drawBehind {
                        this.drawCircle(Color.White, radius = 48f)
                    }
                    .clickable(
                        interactionSource = interactionSource, indication = rememberRipple(
                            bounded = false,
                            radius = 17.dp,
                            color = Color.Transparent
                        )
                    ) {

                    },
                painter = painterResource(id = R.drawable.notification),
                contentDescription = "Notification",
                tint = if (isPressed.value) colorResource(id = R.color.notification_pressed_state) else Color.Unspecified
            )
        }

        CreditCard(
            cardFace = cardFace,
            onClick = {
                cardFace = cardFace.next
            },
            front = {
                CreditCardContent()
            },
            back = {
                Text(text = "Hello World")
            }
        )

        Transactions()
    }
}

@Composable
fun Transactions() {

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(
            "Transactions", style = TextStyle(
                fontSize = 20.sp, color = colorResource(id = R.color.transaction_heading),
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IncomeExpenseCard(
                        resourceId = R.drawable.income,
                        resourceIdColor = colorResource(id = R.color.income_icon),
                        resourceIdBackgroundColor = colorResource(id = R.color.income_icon_background),
                        resourcePercentage = "+24%",
                        resourcePercentageColor = colorResource(id = R.color.income_percentage),
                        resourceType = "Income"
                    )

                    IncomeExpenseCard(
                        resourceId = R.drawable.expense,
                        resourceIdColor = colorResource(id = R.color.expense_icon),
                        resourceIdBackgroundColor = colorResource(id = R.color.expense_icon_background),
                        resourcePercentage = "-24%",
                        resourcePercentageColor = colorResource(id = R.color.expense_percentage),
                        resourceType = "Expense"
                    )
                }
            }

            items(2) {
                TransactionDetails()
            }
        }
    }
}


@Composable
fun CreditCardContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "₹ 3,00,000.00", style = TextStyle(
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 24.sp
            )
        )

        Text(
            text = "Balance", style = TextStyle(
                color = Color.White.copy(0.5f),
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 14.sp
            )
        )

        Row(
            modifier = Modifier
                .padding(top = 30.dp)
                .weight(1f)
        ) {
            RadialGradientLinearProgressIndicator(
                modifier = Modifier,
                progress = 0.5f,
                startColor = colorResource(id = R.color.linear_progress_start),
                endColor = colorResource(id = R.color.linear_progress_end)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = "∗∗∗∗  ∗∗∗∗  ∗∗∗∗  3214", style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                color = colorResource(id = R.color.credit_card_number)
            )

            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = R.drawable.mastercard_logo),
                contentDescription = "Credit Card Logo",
                tint = Color.Unspecified
            )
        }

    }
}

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