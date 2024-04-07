package com.eyeshield.expensetracker.home_graph.compose.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.calendar.models.TransactionData
import com.eyeshield.expensetracker.calendar.shimmerLoadingAnimation

@Composable
fun TransactionDetails(
    modifier: Modifier = Modifier,
    transactionData: TransactionData
) {

    Column(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(20),
            shadowElevation = 1.dp,
            tonalElevation = 1.dp
        ) {

            Row(
                modifier = Modifier
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(id = transactionData.expenseResourceID),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = transactionData.expenseName.orEmpty(), style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                            color = colorResource(id = R.color.transaction_name),
                            fontWeight = FontWeight.W800,
                        ),
                        modifier = Modifier
                    )
                    Text(
                        text = transactionData.expenseDate.orEmpty(), style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                            color = colorResource(id = R.color.transaction_date)
                        ), modifier = Modifier
                    )
                }

                Text(
                    modifier = Modifier,
                    text = transactionData.expenseAmount.orEmpty(), style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                        color = colorResource(id = R.color.transaction_amount),
                        fontWeight = FontWeight.W800
                    )
                )
            }

        }
    }

}

@Composable
@Preview
fun TransactionDetailsShimmer(
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(20),
            shadowElevation = 1.dp,
            tonalElevation = 1.dp
        ) {

            Row(
                modifier = Modifier
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shimmerLoadingAnimation(),
                )

                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(15.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .shadow(5.dp)
                            .shimmerLoadingAnimation()
                    )
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(15.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .shadow(5.dp)
                            .shimmerLoadingAnimation()
                    )
                }

                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(15.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shadow(5.dp)
                        .shimmerLoadingAnimation()
                )
            }

        }
    }

}

@Composable
@Preview
fun TransactionDetailsPreview() {
    TransactionDetails(
        transactionData = TransactionData(
            expenseResourceID = R.drawable.internet,
            expenseName = "Broadband",
            expenseDate = "07 Oct, 2001",
            expenseAmount = "- ₹2500 ",
            expenseId = "2"
        )
    )
}