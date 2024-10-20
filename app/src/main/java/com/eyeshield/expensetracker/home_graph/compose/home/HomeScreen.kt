package com.eyeshield.expensetracker.home_graph.compose.home


import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eyeshield.expensetracker.MainNavRoutes
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.calendar.models.TransactionData
import com.eyeshield.expensetracker.cards.CardInfo
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(mainNavController: NavController) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val showLogOutDialog = remember { mutableStateOf(false) }
    val cardsListSize = remember {
        mutableIntStateOf(3)
    }

    val cardColors: List<Int> = remember {
        listOf(
            R.color.card_color_1,
            R.color.card_color_2,
            R.color.card_color_3
        )
    }
    val cardInfoList = remember {
        mutableStateListOf<CardInfo>()
    }

    val selectedCard by remember {
        derivedStateOf {
            mutableStateOf(
                cardInfoList[0]
            )
        }
    }

    LaunchedEffect(Unit) {
        repeat(cardsListSize.intValue) { index ->
            cardInfoList.add(
                CardInfo(
                    position = index,
                    zIndex = index * 1f,
                    offsetY = index * 30.dp,
                    cardColor = cardColors[index]
                )
            )
        }
    }

    BackHandler {
        showLogOutDialog.value = !showLogOutDialog.value
    }

    val swapCardDetailsOnCardClick: (Int, CardInfo) -> CardInfo =
        remember {
            { index, selectedCard ->


                val getCardDataForMaximumCard = cardInfoList.maxByOrNull {
                    it.zIndex
                }

                val maximumItemIndex = cardInfoList.indexOf(
                    getCardDataForMaximumCard
                )

                val temp = cardInfoList[index].copy()

                cardInfoList[index] =
                    cardInfoList[maximumItemIndex].copy(
                        zIndex = cardInfoList[maximumItemIndex].zIndex,
                        offsetY = cardInfoList[maximumItemIndex].offsetY,
                        cardColor = cardInfoList[index].cardColor,
                        position = cardInfoList[maximumItemIndex].position
                    )

                cardInfoList[maximumItemIndex] = temp.copy(
                    zIndex = temp.zIndex,
                    offsetY = temp.offsetY,
                    cardColor = cardInfoList[maximumItemIndex].cardColor,
                    position = temp.position
                )

                selectedCard

            }
        }

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
                        drawCircle(Color.White, radius = 48f)
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
                tint = if (isPressed.value)
                    colorResource(id = R.color.notification_pressed_state)
                else
                    Color.Unspecified
            )
        }

        Box {
            cardInfoList.forEachIndexed { index, cardPositionAndOffsetState ->

                key(cardPositionAndOffsetState) {

                    var cardFace by remember { mutableStateOf(CardFace.Front) }

                    CreditCard(
                        modifier = Modifier
                            .zIndex(cardPositionAndOffsetState.zIndex)
                            .absoluteOffset(y = cardPositionAndOffsetState.offsetY),
                        cardContainerColor = colorResource(
                            cardPositionAndOffsetState.cardColor
                        ),
                        cardFace = cardFace,
                        onClick = {
                            if (cardPositionAndOffsetState.position == cardInfoList.size - 1) {
                                cardFace = cardFace.next
                            } else {
                                selectedCard.value =
                                    swapCardDetailsOnCardClick(
                                        index,
                                        cardPositionAndOffsetState
                                    ).copy()
                            }
                        },
                        front = {
                            CreditCardContent(mainNavController)
                        },
                        back = {

                        }
                    )
                }


            }
        }

        Spacer(modifier = Modifier.padding(bottom = 5.dp * 3))

        Transactions()
    }
}

@Composable
fun Transactions() {

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val interactionSource = remember {
                MutableInteractionSource()
            }

            val isPressed = interactionSource.collectIsPressedAsState()

            Text(
                modifier = Modifier.weight(1f),
                text = "Transactions", style = TextStyle(
                    fontSize = 20.sp, color = colorResource(id = R.color.transaction_heading),
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )
            )

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterVertically)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {

                        }
                    ),
                painter = painterResource(id = R.drawable.ic_transaction_details),
                contentDescription = "Notification",
                tint = if (isPressed.value) Color.Gray.copy(0.6f) else colorResource(id = R.color.notification_pressed_state)
            )

        }


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
                TransactionDetails(
                    transactionData = TransactionData(
                        expenseResourceID = R.drawable.spotify_icon,
                        expenseName = "Spotify Premium",
                        expenseDate = "Sep 21, 2024",
                        expenseAmount = "- ₹2500",
                        expenseId = "1"
                    ),
                )
            }
        }
    }
}


@Composable
fun CreditCardContent(mainNavController: NavController) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = "₹ 3,00,000.00", style = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 24.sp
                )
            )

            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        mainNavController.navigate(MainNavRoutes.StatisticsScreen.route)
                    },
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "More",
                tint = if (isPressed.value) Color.Gray.copy(0.7f) else Color.Unspecified
            )
        }


        Text(
            text = "Balance: ₹ 58000.00", style = TextStyle(
                color = Color.White.copy(0.5f),
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 14.sp
            )
        )


        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .weight(1f)
        ) {
            RadialGradientLinearProgressIndicator(
                modifier = Modifier,
                progress = (300000f - 58000f) / 300000f,
                startColor = colorResource(id = R.color.linear_progress_start),
                endColor = colorResource(id = R.color.linear_progress_end)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 12.sp
                        )
                    ) {
                        append("∗∗∗∗  ∗∗∗∗  ∗∗∗∗ ")
                    }

                    append(" 3214")
                }, style = TextStyle(
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

@Preview(showBackground = true, backgroundColor = 0xFFF6F6F6)
@Composable
fun HomeScreenPreview() {
    HomeScreen(mainNavController = rememberNavController())
}