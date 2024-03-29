package com.eyeshield.expensetracker.calendar


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.eyeshield.expensetracker.calendar.models.TransactionData
import com.eyeshield.expensetracker.home_graph.compose.home.TransactionDetails

@Composable
@Preview(showBackground = true)
fun CalendarScreen() {

    val items = remember {
        mutableStateListOf(
            TransactionData(
                expenseId = "0",
                expenseResourceID = R.drawable.internet,
                expenseName = "Broadband",
                expenseDate = "07 Oct, 2001",
                expenseAmount = "- ₹2500 "
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {

        PaymentReminderCalendar(modifier = Modifier)

        Text(
            modifier = Modifier,
            text = "Expenses", style = TextStyle(
                fontSize = 20.sp, color = colorResource(id = R.color.transaction_heading),
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            )
        )


        LazyColumn(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(15.dp)) {

            items(items.size.coerceAtMost(2), key = { it }) {
                TransactionDetails(
                    transactionData = items[it], modifier = Modifier
                )
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp), contentAlignment = Alignment.BottomEnd
    ) {
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black.copy(0.9f)
            ),
            shape = RoundedCornerShape(50),
            onClick = {
                items.add(
                    0,
                    TransactionData(
                        expenseId = "${items.size + 1}",
                        expenseResourceID = R.drawable.internet,
                        expenseName = "Broadband",
                        expenseDate = "07 Nov, 2001",
                        expenseAmount = "- ₹2500 "
                    )
                )
            }) {
            Text(
                text = "Record a Expense", style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.nunito_bold)), fontSize = 16.sp
                )
            )
        }
    }
}
