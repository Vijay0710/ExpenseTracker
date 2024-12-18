package com.eyeshield.expensetracker.home_graph.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.calendar_graph.data.TransactionData

@Composable
fun Transactions() {

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val interactionSource = remember {
                MutableInteractionSource()
            }

            val isPressed = interactionSource.collectIsPressedAsState()

            Text(
                modifier = Modifier.weight(1f),
                text = "Transactions", style = TextStyle(
                    fontSize = 20.sp, color = colorResource(id = R.color.transaction_heading),
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )
            )

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterVertically)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {

                        }
                    ),
                painter = painterResource(id = R.drawable.ic_transaction_details),
                contentDescription = "Notification",
                tint = if (isPressed.value) Color.Gray.copy(0.6f) else colorResource(id = R.color.notification_pressed_state)
            )

        }


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IncomeExpenseCard(
                        resourceId = R.drawable.income,
                        resourceIdColor = colorResource(id = R.color.income_icon),
                        resourceIdBackgroundColor = colorResource(id = R.color.income_icon_background),
                        resourcePercentage = "+24%",
                        resourcePercentageColor = colorResource(id = R.color.income_percentage),
                        resourceType = "Income"
                    )

                    IncomeExpenseCard(
                        resourceId = R.drawable.expense,
                        resourceIdColor = colorResource(id = R.color.expense_icon),
                        resourceIdBackgroundColor = colorResource(id = R.color.expense_icon_background),
                        resourcePercentage = "-24%",
                        resourcePercentageColor = colorResource(id = R.color.expense_percentage),
                        resourceType = "Expense"
                    )
                }
            }

            items(2) {
                TransactionDetails(
                    transactionData = TransactionData(
                        expenseResourceID = R.drawable.spotify_icon,
                        expenseName = "Spotify Premium",
                        expenseDate = "Sep 21, 2024",
                        expenseAmount = "- ₹2500",
                        expenseId = "1"
                    ),
                )
            }
        }
    }
}