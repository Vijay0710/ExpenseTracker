package com.eyeshield.expensetracker.home_graph.compose.statistics


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.calendar.models.TransactionData
import com.eyeshield.expensetracker.home.SmoothLineGraph
import com.eyeshield.expensetracker.home_graph.compose.home.TransactionDetails


@Composable
fun StatisticsScreen(navController: NavController) {
    val backIcon = remember { MutableInteractionSource() }
    val items = listOf("Week", "Month", "Year")
    val statisticsBackgroundColor = colorResource(id = R.color.statistics_segmented_button_bg)

    val selectedSegment = remember { mutableStateOf(items[1]) }
    val selectedSegmentBackgroundModifier = remember {
        Modifier
            .background(
                statisticsBackgroundColor,
                shape = RoundedCornerShape(50)
            )
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .background(color = colorResource(id = R.color.shadow_white))
            .verticalScroll(rememberScrollState())
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
                        interactionSource = backIcon, indication = rememberRipple(
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
                text = "Statistics", style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W800
                )
            )

            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .drawBehind {
                        this.drawCircle(Color.White, radius = 40f)
                    }
                    .clickable(
                        interactionSource = backIcon, indication = rememberRipple(
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

        Text(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(),
            text = "₹ 3,00,000.00", style = TextStyle(
                fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        )

        Text(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            text = "Sep 16, 2021", style = TextStyle(
                fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            ),
            color = colorResource(id = R.color.statistics_date)
        )


        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(50)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) {
                Spacer(modifier = Modifier.padding(vertical = 20.dp))
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                bounded = true,
                            )
                        ) {
                            selectedSegment.value = items[it]
                        }
                        .then(if (selectedSegment.value == items[it]) selectedSegmentBackgroundModifier else Modifier)
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    text = items[it],
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_semi_bold))
                    ),
                    color = if (selectedSegment.value == items[it]) colorResource(R.color.white) else colorResource(
                        id = R.color.statistics_date
                    )
                )
                Spacer(modifier = Modifier.padding(end = 10.dp))
            }
        }

        SmoothLineGraph()

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "Top Spending", style = TextStyle(
                fontSize = 20.sp, color = colorResource(id = R.color.transaction_heading),
                fontFamily = FontFamily(Font(R.font.nunito_bold))
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            repeat(2) {
                TransactionDetails(
                    transactionData = TransactionData(
                        expenseResourceID = R.drawable.spotify_icon,
                        expenseName = "Spotify Premium",
                        expenseDate = "Sep 21, 2024",
                        expenseAmount = "- ₹2500",
                        expenseId = "2"
                    ),
                )
            }
        }

    }

}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF6F6F6)
private fun PreviewStatisticsScreen() {
    StatisticsScreen(navController = rememberNavController())
}