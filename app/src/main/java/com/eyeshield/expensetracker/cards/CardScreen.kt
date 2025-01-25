package com.eyeshield.expensetracker.cards

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.cards.data.CardsInfoUIModel
import com.eyeshield.expensetracker.cards.data.CreditCardInfoUIModel
import com.eyeshield.expensetracker.common.AnalyzeIcon
import com.eyeshield.expensetracker.common.ChevronForward
import com.eyeshield.expensetracker.data.mapper.toCreditAccountUIModel
import com.eyeshield.expensetracker.extensions.bottomPadding
import com.eyeshield.expensetracker.extensions.horizontalPadding
import com.eyeshield.expensetracker.extensions.topPadding
import com.eyeshield.expensetracker.home_graph.home.components.CardFace
import com.eyeshield.expensetracker.home_graph.home.components.CreditCard
import com.eyeshield.expensetracker.home_graph.home.components.CreditCardContent
import kotlin.math.absoluteValue

@SuppressLint("RestrictedApi")
@Composable
fun CardScreen(
    uiState: CardsViewModel.UIState,
    uiAction: (CardsViewModel.UIAction) -> Unit
) {
    val state = rememberPagerState {
        uiState.cardsList.size
    }

    val colorList = remember {
        listOf(
            R.color.card_color_1,
            R.color.card_color_2,
            R.color.card_color_3
        )
    }

    LaunchedEffect(uiState.cardsList.size) {
        if (uiState.cardsList.size > 1) {
            state.scrollToPage(1)
        }
    }

    LaunchedEffect(uiState.cardsList.size, state.settledPage) {
        if (uiState.cardsList.isNotEmpty()) {
            uiAction.invoke(
                CardsViewModel.UIAction.VisibleCard(
                    uiState.cardsList[state.settledPage].id,
                    uiState.cardsList[state.settledPage].bankName
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .background(color = colorResource(R.color.card_screen_background))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = uiState.cardTitle,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .topPadding(30.dp)
        )

        HorizontalPager(
            modifier = Modifier,
            state = state,
            contentPadding = PaddingValues(28.dp)
        ) { page ->
            key(page) {
                var cardFace by remember(page) {
                    mutableStateOf(CardFace.Front)
                }
                CreditCard(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset =
                                ((state.currentPage - page) + state.currentPageOffsetFraction).absoluteValue

                            scaleY = lerp(
                                start = 0.8f,
                                stop = 1f,
                                amount = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                        .horizontalPadding(5.dp),
                    cardContainerColor = colorResource(
                        colorList[page % colorList.size]
                    ),
                    front = {
                        CreditCardContent(
                            onNavigate = {},
                            accountInfo = uiState.cardsList[page].toCreditAccountUIModel()
                        )
                    },
                    back = {

                    },
                    cardFace = cardFace,
                    onClick = {
                        cardFace = cardFace.next
                    }
                )
            }
        }

        TextWithIcon(
            modifier = Modifier
                .bottomPadding(12.dp)
                .horizontalPadding(15.dp),
            title = "Check out Transactions",
            titleColor = colorResource(R.color.check_out_transactions),
            icon = ChevronForward,
            iconColor = colorResource(R.color.check_out_transactions)
        )

        TextWithIcon(
            modifier = Modifier
                .bottomPadding(20.dp)
                .horizontalPadding(15.dp),
            title = "Analyse Spends",
            titleColor = colorResource(R.color.check_out_transactions),
            icon = AnalyzeIcon,
            iconColor = colorResource(R.color.check_out_transactions)
        )

        CardInformationSection(
            uiState.selectedCardInfo
        )
    }
}

@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    title: String = "",
    titleColor: Color,
    icon: ImageVector,
    iconColor: Color
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            modifier = textModifier.align(Alignment.CenterVertically),
            text = title,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 15.sp,
                color = titleColor
            )
        )

        Spacer(modifier = Modifier.width(5.dp))

        Icon(
            imageVector = icon,
            contentDescription = "Chevron Forward",
            tint = iconColor
        )
    }
}

@Composable
fun ColumnScope.CardInformationSection(
    cardInfo: CardsInfoUIModel
) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .shadow(
                elevation = 12.dp,
                clip = false,
                shape = RoundedCornerShape(
                    topStart = 70.dp,
                    topEnd = 70.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 1.dp
                ),
                spotColor = colorResource(R.color.login_surface_ambient_shadow_color).copy(
                    0.1f
                )
            ),
        shape = RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp),
        color = colorResource(R.color.login_screen_background)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Card Information",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .topPadding(30.dp)
            ) {

                CardInformationSectionItem(
                    title = "Total Limit",
                    value = cardInfo.totalLimit
                )
                Spacer(modifier = Modifier.height(20.dp))


                CardInformationSectionItem(
                    title = "Billing Cycle",
                    value = cardInfo.billingCycle
                )
                Spacer(modifier = Modifier.height(20.dp))

                CardInformationSectionItem(
                    title = "Due Date",
                    value = cardInfo.dueDate
                )
                Spacer(modifier = Modifier.height(20.dp))

                CardInformationSectionItem(
                    title = "Last Due",
                    value = cardInfo.lastDueDate
                )
                Spacer(modifier = Modifier.height(20.dp))

                CardInformationSectionItem(
                    title = "Total Reward Points",
                    value = cardInfo.totalRewardsPoints
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun CardInformationSectionItem(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalPadding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            style = TextStyle(
                fontSize = 16.sp,
                color = colorResource(R.color.white),
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
            )
        )

        Text(
            text = value,
            style = TextStyle(
                fontSize = 16.sp,
                color = colorResource(R.color.white),
                fontFamily = FontFamily(Font(R.font.nunito_regular))
            )
        )
    }
}

@Preview
@Composable
private fun PreviewCardsScreen() {
    CardScreen(
        uiState = CardsViewModel.UIState(
            cardTitle = "HDFC Millenia Card",
            cardsList = listOf(
                CreditCardInfoUIModel(
                    id = "1234",
                    accountNumber = "•••• •••• •••• 1234",
                    progress = 0.6f,
                    cardLimit = "₹ 2,00,000",
                    logo = R.drawable.visa_logo,
                    creditCardOutStanding = "15000.00",
                    bankName = "HDFC Millenia Debit Card"
                ),
                CreditCardInfoUIModel(
                    id = "1234",
                    accountNumber = "•••• •••• •••• 1234",
                    progress = 0.6f,
                    cardLimit = "₹ 2,00,000",
                    logo = R.drawable.visa_logo,
                    creditCardOutStanding = "15000.00",
                    bankName = "HDFC Millenia Debit Card"
                ),
                CreditCardInfoUIModel(
                    id = "1234",
                    accountNumber = "•••• •••• •••• 1234",
                    progress = 0.4f,
                    cardLimit = "₹ 2,00,000",
                    logo = R.drawable.visa_logo,
                    creditCardOutStanding = "15000.00",
                    bankName = "HDFC Millenia Debit Card"
                )
            ),
            selectedCardInfo = CardsInfoUIModel(
                totalLimit = "₹ 2,00,000",
                billingCycle = "19/11/2024 - 20/12/2024",
                dueDate = "07/01/2025",
                lastDueDate = "07/12/2024",
                totalRewardsPoints = "10,000"
            )

        ),
        uiAction = {

        }
    )
}
