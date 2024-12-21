package com.eyeshield.expensetracker.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.common.CommonButton
import com.eyeshield.expensetracker.extensions.startPadding
import com.eyeshield.expensetracker.extensions.topPadding
import com.eyeshield.expensetracker.home_graph.home.components.CardFace
import com.eyeshield.expensetracker.home_graph.home.components.CreditCard

@Composable
@Preview
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onNextClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(
                color = colorResource(R.color.login_screen_background)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .fillMaxSize()
                    .topPadding(60.dp)
            ) {
                repeat(2) {
                    CreditCard(
                        modifier = Modifier
                            .offset(
                                x = it * 10.dp,
                                y = it * 15.dp
                            )
                            .rotate(-20f)
                            .startPadding(if (it == 1) 10.dp else 0.dp)
                            .padding(30.dp),
                        cardFace = CardFace.Front,
                        cardContainerColor = colorResource(R.color.welcome_screen_card_container_color),
                        shouldShowCircles = false,
                        internalPaddingValues = PaddingValues(
                            if (it == 0) 0.dp else 0.dp
                        ),
                        borderStroke = BorderStroke(
                            1.dp,
                            color = colorResource(R.color.welcome_screen_card_border)
                        ),
                        front = {
                            FrontCardGradient(
                                index = it
                            )
                        },
                        back = {

                        },
                        onClick = {

                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .topPadding(300.dp),
                        text = "EXPENSE TRACKER",
                        style = TextStyle(
                            fontSize = 64.sp,
                            fontFamily = FontFamily(Font(R.font.rubik_doodle_shadow_regular)),
                            color = colorResource(R.color.white)
                        )
                    )

                    Text(
                        modifier = Modifier.topPadding(20.dp),
                        text = "Your ultimate app for managing expenses on the go. Track spending, stay organized, and gain control over your finances.",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 20.sp,
                            lineHeight = 30.sp,
                            color = colorResource(R.color.welcome_screen_expense_tracker_description)
                        )
                    )

                    CommonButton(
                        modifier = Modifier
                            .topPadding(30.dp)
                            .height(58.dp)
                            .width(180.dp),
                        title = stringResource(R.string.welcome_screen_next_cta),
                        onClick = onNextClick,
                        enabled = true
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 50.dp, y = (-40).dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(250.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    colorResource(R.color.login_screen_blur_circle_color_1),
                                    colorResource(R.color.login_screen_blur_circle_color_2)
                                )
                            )
                        )
                )
            }
        }
    }


}

@Composable
fun FrontCardGradient(
    index: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp), contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(60.dp)
                .blur(86.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colorResource(R.color.login_screen_blur_circle_color_1),
                            colorResource(R.color.login_screen_blur_circle_color_2)
                        )
                    )
                )
        )

        if (index == 1) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(100.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.mastercard_logo),
                    contentDescription = "MasterCard Logo",
                    tint = Color.Unspecified,
                )

                Text(
                    modifier = Modifier
                        .offset(y = (-10).dp)
                        .startPadding(10.dp),
                    text = "My Wallet",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                        color = colorResource(R.color.welcome_screen_my_wallet_placeholder),
                        fontSize = 24.sp
                    )
                )

                Text(
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .startPadding(10.dp),
                    text = "••••  ••••  ••••  7010",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        color = colorResource(R.color.welcome_screen_my_wallet_placeholder),
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}