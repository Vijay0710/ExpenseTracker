package com.eyeshield.expensetracker.calendar.expense

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.calendar.models.TransactionData
import com.eyeshield.expensetracker.home_graph.compose.home.TransactionDetails

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF6F6F6)
fun AddExpenseScreen(navController: NavController = rememberNavController()) {
    val backIconInteractionSource = remember { MutableInteractionSource() }
    val addItemInteractionSource = remember { MutableInteractionSource() }
    val addItemPressedState = addItemInteractionSource.collectIsPressedAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.record_expense_bg))
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .drawBehind {
                            this.drawCircle(Color.White, radius = 40f)
                        }
                        .clickable(
                            interactionSource = backIconInteractionSource,
                            indication = ripple(
                                bounded = false,
                                radius = 12.dp,
                                color = Color.Gray.copy(0.5f)
                            )
                        ) {
                            navController.popBackStack()
                        },
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "Back",
                    tint = Color.Unspecified
                )

                Text(
                    text = "Create Expense", style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W800,
                        color = Color.White
                    )
                )

                Icon(
                    modifier = Modifier
                        .size(15.dp)
                        .drawBehind {
                            this.drawCircle(Color.White, radius = 40f)
                        }
                        .clickable(
                            interactionSource = backIconInteractionSource,
                            indication = ripple(
                                bounded = false,
                                radius = 12.dp,
                                color = Color.Gray.copy(0.5f)
                            )
                        ) {
                            navController.popBackStack()
                        },
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = "Back"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding(bottom = 30.dp))
                Text(
                    text = "Invoice# bT28HuFL", style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        fontSize = 24.sp,
                        color = Color.White,
                    )
                )

                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Sep 28, 2001", style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.transaction_date)
                    )
                )
            }

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "Bill To", style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                    fontSize = 22.sp,
                    color = Color.White
                )
            )

            TransactionDetails(
                modifier = Modifier.padding(top = 20.dp), transactionData = TransactionData(
                    expenseId = "0",
                    expenseName = "Android Dev Course",
                    expenseDate = "31 Mar, 2024",
                    expenseAmount = "₹499",
                    expenseResourceID = R.drawable.logo_udemy
                )
            )

            val stroke = Stroke(
                width = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )

            Spacer(modifier = Modifier.padding(top = 25.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = Color.White,
                            style = stroke,
                            cornerRadius = CornerRadius(8.dp.toPx())
                        )
                    },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = BorderStroke(0.dp, Color.Transparent),
                interactionSource = addItemInteractionSource,
                onClick = {

                },
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add Icon",
                    tint = if (addItemPressedState.value) Color.Gray.copy(0.6f) else Color.White
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Add Items", style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                        color = if (addItemPressedState.value) Color.Gray.copy(0.6f) else Color.White
                    )
                )
            }


            Text(
                modifier = Modifier.padding(top = 30.dp),
                text = "Pricing", style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                    fontSize = 22.sp,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    PricingSection(pricingType = "Subtotal", amount = "₹499.05")
                    PricingSection(pricingType = "Tax", amount = "₹20")
                    PricingSection(pricingType = "Discount", amount = "-₹10")
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Total", style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W800,
                            color = colorResource(id = R.color.pricing_type)
                        )
                    )
                    Text(
                        text = "₹ 500.05", style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.W800
                        )
                    )
                }

                Button(modifier = Modifier

                    .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black.copy(0.9f)
                    ),
                    shape = RoundedCornerShape(50),
                    onClick = {
                    }) {
                    Text(
                        text = "Save Invoice", style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }


}

@Composable
fun PricingSection(pricingType: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = pricingType, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 14.sp,
                fontWeight = FontWeight.W800,
                color = colorResource(id = R.color.pricing_type)
            )
        )
        Text(
            text = amount, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                fontSize = 14.sp,
                fontWeight = FontWeight.W800
            )
        )
    }
}
