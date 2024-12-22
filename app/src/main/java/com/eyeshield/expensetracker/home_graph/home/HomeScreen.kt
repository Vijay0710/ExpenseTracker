package com.eyeshield.expensetracker.home_graph.home


import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.application.MainNavRoutes
import com.eyeshield.expensetracker.common.AnimatedToast
import com.eyeshield.expensetracker.home_graph.home.components.CardFace
import com.eyeshield.expensetracker.home_graph.home.components.CardShimmer
import com.eyeshield.expensetracker.home_graph.home.components.CreditCard
import com.eyeshield.expensetracker.home_graph.home.components.CreditCardContent
import com.eyeshield.expensetracker.home_graph.home.components.Transactions
import com.eyeshield.expensetracker.home_graph.home.data.CardInfo
import com.eyeshield.expensetracker.home_graph.home.data.network.CreditAccountResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (MainNavRoutes) -> Unit,
    uiState: HomeViewModel.UiState,
    uiAction: (HomeViewModel.UiAction) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val showLogOutDialog = remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    // To show 3D effect in Y Axis when user scrolls to the end of cards
    var rotationYAxis by remember {
        mutableFloatStateOf(0f)
    }

    BackHandler {
        showLogOutDialog.value = !showLogOutDialog.value
    }

    PullToRefreshBox(
        modifier = Modifier,
        isRefreshing = uiState.isPullToRefreshInProgress,
        onRefresh = {
            uiAction(HomeViewModel.UiAction.OnPullToRefreshClicked)
        },
        state = pullToRefreshState,
        indicator = {
            Indicator(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = uiState.isPullToRefreshInProgress,
                containerColor = colorResource(R.color.shadow_white),
                color = colorResource(R.color.login_screen_background)
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
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
                    CardShimmer()
                } else {
                    Box(
                        modifier = Modifier
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

                                detectVerticalDragGestures(
                                    onVerticalDrag = { _, dragAmount ->
                                        rotationYAxis =
                                            (rotationYAxis + dragAmount / 5).coerceIn(-20f, 20f)
                                    },
                                    onDragEnd = {
                                        rotationYAxis = 0f
                                    },
                                    onDragCancel = {
                                        rotationYAxis = 0f
                                    }
                                )
                            }
                    ) {
                        uiState.creditAccounts.zip(uiState.cardInfoList)
                            .mapIndexed { index, cardPositionAndOffsetState ->
                                key(index) {
                                    var cardFace by remember { mutableStateOf(CardFace.Front) }

                                    val zOffsetDelay = remember(index) { 100 * index }
                                    val yOffsetDelay =
                                        remember(index) { 2 * zOffsetDelay + (100 * uiState.creditAccounts.size - 1) }

                                    val animateYOffset = animateDpAsState(
                                        targetValue = cardPositionAndOffsetState.second.offsetY,
                                        label = "Y Offset Animation",
                                        animationSpec = tween(
                                            500,
                                            easing = EaseIn,
                                            delayMillis = yOffsetDelay
                                        )
                                    )

                                    val animateZIndex = animateFloatAsState(
                                        targetValue = cardPositionAndOffsetState.second.zIndex,
                                        label = "Z index animation",
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            easing = EaseIn,
                                            delayMillis = zOffsetDelay
                                        )
                                    )

                                    CreditCard(
                                        modifier = Modifier
                                            .zIndex(animateZIndex.value)
                                            .offset {
                                                IntOffset(
                                                    x = 0,
                                                    y = animateYOffset.value.roundToPx()
                                                )
                                            },
                                        cardContainerColor = colorResource(
                                            cardPositionAndOffsetState.second.cardColor
                                        ),
                                        cardFace = cardFace,
                                        onClick = {
                                            if (cardPositionAndOffsetState.second.position == uiState.cardInfoList.size - 1) {
                                                cardFace = cardFace.next
                                            } else {
                                                uiAction(
                                                    HomeViewModel.UiAction.TransformCreditCards(
                                                        index = index,
                                                        selectedCard = cardPositionAndOffsetState.second
                                                    )
                                                )
                                            }
                                        },
                                        front = {
                                            CreditCardContent(
                                                onNavigate = onNavigate,
                                                accountInfo = cardPositionAndOffsetState.first
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
                if (!uiState.isLoading && uiState.creditAccounts.size > 1) {
                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )
                }
            }

            item {
                Transactions()
            }
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


@Preview(showBackground = true, backgroundColor = 0xFFF6F6F6)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigate = {
            // Preview
        },
        uiState = HomeViewModel.UiState(
            isLoading = false,
            creditAccounts = listOf(
                CreditAccountResponse(
                    creditCardLimit = "200000",
                    creditCardOutStanding = 15000f
                ),
                CreditAccountResponse(
                    creditCardLimit = "200000",
                    creditCardOutStanding = 15000f
                ),
                CreditAccountResponse(
                    creditCardLimit = "200000",
                    creditCardOutStanding = 15000f,
                    accountNumber = "1234567890123456",
                    cardType = "MASTERCARD"
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