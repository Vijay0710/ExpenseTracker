package com.eyeshield.expensetracker.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
@Preview
fun TransactionDetails() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(20)
    ) {

        Row(
            modifier = Modifier
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_play),
                contentDescription = null
            )

            Column(modifier = Modifier) {
                Text(
                    text = "Spotify Premium", style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                        color = colorResource(id = R.color.transaction_name)
                    )
                )
                Text(
                    text = "07 Oct, 2001", style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                        color = colorResource(id = R.color.transaction_date)
                    )
                )
            }

            Text(
                modifier = Modifier,
                text = "- ₹2500", style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                    fontWeight = FontWeight.ExtraBold,
                    color = colorResource(id = R.color.transaction_amount)
                )
            )
        }

    }
}