package com.eyeshield.expensetracker.home_graph.compose.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                        text = transactionData.expenseName, style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                            color = colorResource(id = R.color.transaction_name),
                            fontWeight = FontWeight.W800,
                        )
                    )
                    Text(
                        text = transactionData.expenseDate, style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                            color = colorResource(id = R.color.transaction_date)
                        )
                    )
                }

                Text(
                    modifier = Modifier,
                    text = transactionData.expenseAmount, style = TextStyle(
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
fun TransactionDetailsPreview() {
    TransactionDetails(
        transactionData = TransactionData(
            expenseResourceID = R.drawable.internet,
            expenseName = "Broadband",
            expenseDate = "07 Oct, 2001",
            expenseAmount = "- ₹2500 "
        )
    )
}