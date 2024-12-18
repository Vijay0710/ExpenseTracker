package com.eyeshield.expensetracker.home_graph.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.calendar_graph.shimmerLoadingAnimation
import com.eyeshield.expensetracker.extensions.endPadding
import com.eyeshield.expensetracker.extensions.topPadding

@Composable
@Preview
fun CardShimmer() {
    CreditCard(
        modifier = Modifier,
        cardBoxModifier = Modifier
            .background(color = colorResource(R.color.white).copy(0.5f))
            .shimmerLoadingAnimation(),
        cardFace = CardFace.Front,
        shouldShowCircles = false,
        front = {
            CreditCardContentShimmer()
        },
        back = {

        },
        onClick = {

        }
    )
}

@Composable
fun CreditCardContentShimmer() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .endPadding(150.dp)
                    .shimmerLoadingAnimation(
                        shape = RoundedCornerShape(15.dp)
                    ),
                text = "", style = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 24.sp
                )
            )
        }


        Text(
            modifier = Modifier
                .height(12.dp)
                .shimmerLoadingAnimation(

                ),
            text = "", style = TextStyle(
                color = Color.White.copy(0.5f),
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 14.sp
            )
        )


        Box(
            modifier = Modifier
                .topPadding(20.dp)
                .fillMaxWidth()
                .height(20.dp)
                .shimmerLoadingAnimation(
                    shape = RoundedCornerShape(20.dp)
                )

        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .topPadding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .shimmerLoadingAnimation(
                        shape = RoundedCornerShape(20.dp)
                    )
            )

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .shimmerLoadingAnimation(
                        shape = RoundedCornerShape(10.dp)
                    ),
            )
        }

    }
}