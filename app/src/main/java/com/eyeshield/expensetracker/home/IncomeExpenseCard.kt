package com.eyeshield.expensetracker.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R

@Composable
fun RowScope.IncomeExpenseCard(
    @DrawableRes resourceId: Int,
    resourceIdColor: Color,
    resourceIdBackgroundColor: Color,
    resourcePercentage: String,
    resourcePercentageColor: Color,
    resourceType: String
) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .height(80.dp),
        shape = RoundedCornerShape(20),
        color = Color.White,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .weight(1.5f)
                    .size(18.dp)
                    .drawBehind {
                        drawCircle(resourceIdBackgroundColor, radius = 48F)
                    },
                //e3fcea
                painter = painterResource(id = resourceId),
                contentDescription = "Income Icon",
                tint = resourceIdColor
            )

            Column(
                modifier = Modifier.weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = resourcePercentage, style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                        fontSize = 15.sp
                    ),
                    color = resourcePercentageColor
                )

                Text(
                    text = resourceType, style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                        fontSize = 15.sp
                    ),
                    color = colorResource(id = R.color.income_or_expense_text)
                )
            }

        }
    }
}

@Composable
@Preview
private fun PreviewIncomeCard() {
    Row {
        IncomeExpenseCard(
            resourceId = R.drawable.income,
            resourceIdColor = Color(0xFF36e66b),
            resourceIdBackgroundColor = Color(0xFFe3fcea),
            resourcePercentage = "+24%",
            resourcePercentageColor = Color(0xFF1fe35b),
            resourceType = "Income"
        )
    }
}

@Composable
@Preview
private fun PreviewExpenseCard() {
    Row {
        IncomeExpenseCard(
            resourceId = R.drawable.expense,
            resourceIdColor = Color(0xFFff9902),
            resourceIdBackgroundColor = Color(0xFFfff0d9),
            resourcePercentage = "-24%",
            resourcePercentageColor = Color(0xFFff8501),
            resourceType = "Expense"
        )
    }
}