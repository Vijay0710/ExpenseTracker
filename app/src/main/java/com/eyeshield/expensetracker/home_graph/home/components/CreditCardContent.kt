package com.eyeshield.expensetracker.home_graph.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.application.MainNavRoutes
import com.eyeshield.expensetracker.home_graph.home.data.network.CreditAccountResponse

@Composable
fun CreditCardContent(
    accountInfo: CreditAccountResponse,
    onNavigate: (MainNavRoutes) -> Unit
) {
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
                text = accountInfo.formatCreditCardLimit(), style = TextStyle(
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
                        indication = null,
                        onClick = {
                            onNavigate(MainNavRoutes.StatisticsScreen)
                        }
                    ),
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "More",
                tint = if (isPressed.value) Color.Gray.copy(0.7f) else Color.Unspecified
            )
        }

        Text(
            text = accountInfo.creditCardOutStanding.toString(), style = TextStyle(
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
                progress = accountInfo.calculateProgress(),
                startColor = colorResource(id = R.color.linear_progress_start),
                endColor = colorResource(id = R.color.linear_progress_end)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = buildAnnotatedString {
                    append(accountInfo.formatAccountNumberForDisplay())
                }, style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                ),
                color = colorResource(id = R.color.credit_card_number)
            )

            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = ImageVector.vectorResource(id = accountInfo.getLogoType()),
                contentDescription = "Credit Card Logo",
                tint = Color.Unspecified
            )
        }

    }
}