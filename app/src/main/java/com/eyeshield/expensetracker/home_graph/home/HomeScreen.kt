package com.eyeshield.expensetracker.home_graph.home


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.application.Routes
import com.eyeshield.expensetracker.common.AnimatedToast
import com.eyeshield.expensetracker.extensions.bottomPadding
import com.eyeshield.expensetracker.extensions.horizontalPadding
import com.eyeshield.expensetracker.extensions.topPadding
import com.eyeshield.expensetracker.home_graph.home.components.CardFace
import com.eyeshield.expensetracker.home_graph.home.components.CardShimmer
import com.eyeshield.expensetracker.home_graph.home.components.CreditCard
import com.eyeshield.expensetracker.home_graph.home.components.CreditCardContent
import com.eyeshield.expensetracker.home_graph.home.components.Transactions
import com.eyeshield.expensetracker.home_graph.home.data.CardInfo
import com.eyeshield.expensetracker.home_graph.home.data.CreditAccountUIModel
import com.eyeshield.expensetracker.utils.handleEdgeToEdgeInsets

@Composable
fun HomeScreen(
    onNavigate: (Routes) -> Unit,
    uiState: HomeViewModel.UiState,
    uiAction: (HomeViewModel.UiAction) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    // To show 3D effect in Y Axis when user scrolls to the end of cards
    var rotationYAxis by remember {
        mutableFloatStateOf(0f)
    }


    LazyColumn(
        modifier = Modifier
            .horizontalPadding(24.dp)
            .handleEdgeToEdgeInsets(
                isScaffold = false,
                shouldHandleStatusBarInsets = true
            )
            .fillMaxSize()
            .imePadding(),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomPadding(40.dp)
            ) {
                Column(
                    modifier = Modifier
                        .topPadding(12.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "Good Morning!",
                        style = TextStyle(
                            fontSize = 15.sp, color = colorResource(id = R.color.greeting),
                            fontFamily = FontFamily(Font(R.font.nunito_regular))
                        )
                    )

                    Text(
                        modifier = Modifier.topPadding(10.dp),
                        text = "Vijay A",
                        style = TextStyle(
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
                            interactionSource = interactionSource,
                            indication = ripple(
                                bounded = false,
                                radius = 17.dp,
                                color = Color.Transparent
                            ),
                            onClick = {}
                        ),
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = "Notification",
                    tint = if (isPressed.value)
                        colorResource(id = R.color.notification_pressed_state)
                    else
                        Color.Unspecified
                )
            }
        }

        item {
            if (uiState.isLoading) {
                CardShimmer(
                    modifier = Modifier.bottomPadding(40.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .bottomPadding(40.dp)
                        .graphicsLayer {
                            rotationY = rotationYAxis
                            cameraDistance = 20f
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { _, dragAmount ->
                                    rotationYAxis =
                                        (rotationYAxis + dragAmount.x / 5).coerceIn(-20f, 20f)
                                },
                                onDragEnd = {
                                    rotationYAxis = 0f
                                },
                                onDragCancel = {
                                    rotationYAxis = 0f
                                },
                            )
                        }
                ) {
                    uiState.creditAccounts.zip(uiState.cardInfoList)
                        .forEachIndexed { index, (accountInfo, cardPositionAndOffsetState) ->
                            key(accountInfo) {
                                var cardFace by remember { mutableStateOf(CardFace.Front) }


                                val animateYOffset = animateDpAsState(
                                    targetValue = cardPositionAndOffsetState.offsetY,
                                    label = "Y Offset Animation",
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                                )

                                val animatedScale by animateFloatAsState(
                                    targetValue = if (uiState.selectedCard.id == cardPositionAndOffsetState.id) 1.05f else 0.85f + (0.1f * cardPositionAndOffsetState.position),
                                    animationSpec = tween(500, easing = LinearOutSlowInEasing),
                                    label = "Card Scale Animation"
                                )

                                val animateZIndex by animateFloatAsState(
                                    targetValue = cardPositionAndOffsetState.zIndex,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioHighBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    ),
                                    label = "Z index animation"
                                )

                                CreditCard(
                                    modifier = Modifier
                                        .zIndex(animateZIndex)
                                        .offset(y = animateYOffset.value)
                                        .graphicsLayer {
                                            scaleX = animatedScale
                                            scaleY = animatedScale
                                        },
                                    cardContainerColor = colorResource(
                                        cardPositionAndOffsetState.cardColor
                                    ),
                                    cardFace = cardFace,
                                    onClick = {
                                        if (cardPositionAndOffsetState.position == uiState.cardInfoList.size - 1) {
                                            cardFace = cardFace.next
                                        } else {
                                            uiAction(
                                                HomeViewModel.UiAction.TransformCreditCards(
                                                    selectedCard = cardPositionAndOffsetState
                                                )
                                            )
                                        }
                                    },
                                    front = {
                                        CreditCardContent(
                                            onNavigate = onNavigate,
                                            accountInfo = accountInfo
                                        )
                                    },
                                    back = {

                                    }
                                )
                            }
                        }
                }
            }
        }

        item {
            if (uiState.creditAccounts.size > 1) {
                Spacer(
                    modifier = Modifier
                        .bottomPadding(30.dp)
                        .height(5.dp * uiState.creditAccounts.size)
                )
            }
        }

        item {
            Transactions()
        }

        item {
            Spacer(
                modifier = Modifier.handleEdgeToEdgeInsets(
                    isScaffold = false,
                    shouldHandleStatusBarInsets = false,
                    shouldHandleBottomBarInsets = true
                )
            )
        }
    }


    AnimatedToast(
        shouldShowToast = uiState.shouldShowToast,
        errorMessage = uiState.errorMessage,
        onProgressFinished = {
            uiAction.invoke(HomeViewModel.UiAction.OnTrackIndicatorFinished)
        },
        backGroundColor = colorResource(R.color.login_screen_background),
        borderStroke = BorderStroke(1.dp, color = colorResource(R.color.welcome_screen_card_border))
    )
}


@Preview(
    device = "spec:width=1440px,height=3200px,dpi=560",
    showBackground = true,
    backgroundColor = 0xFFF6F6F6
)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigate = {
            // Preview
        },
        uiState = HomeViewModel.UiState(
            isLoading = false,
            creditAccounts = listOf(
                CreditAccountUIModel(
                    id = "1234",
                    accountNumber = "•••• •••• •••• 1234",
                    progress = 0.6f,
                    cardLimit = "₹ 2,00,000",
                    logo = R.drawable.visa_logo,
                    creditCardOutStanding = "15000.00"
                ),
                CreditAccountUIModel(
                    id = "1234",
                    accountNumber = "•••• •••• •••• 1234",
                    progress = 0.6f,
                    cardLimit = "₹ 2,00,000",
                    logo = R.drawable.visa_logo,
                    creditCardOutStanding = "15000.00"
                ),
                CreditAccountUIModel(
                    id = "1234",
                    accountNumber = "•••• •••• •••• 1234",
                    progress = 0.4f,
                    cardLimit = "₹ 2,00,000",
                    logo = R.drawable.visa_logo,
                    creditCardOutStanding = "15000.00"
                )
            ),
            shouldShowToast = false,
            errorMessage = "Oops! Something Went wrong! Please try again later",
            isPullToRefreshInProgress = false,
            cardInfoList = mutableListOf(
                CardInfo(
                    position = 0,
                    zIndex = 0f,
                    offsetY = 0.dp,
                    cardColor = R.color.card_color_1
                ),
                CardInfo(
                    position = 1,
                    zIndex = 1f,
                    offsetY = 30.dp,
                    cardColor = R.color.card_color_2
                ),
                CardInfo(
                    position = 2,
                    zIndex = 2f,
                    offsetY = 60.dp,
                    cardColor = R.color.card_color_3
                )
            )
        ),
        uiAction = {

        }
    )
}