package com.eyeshield.expensetracker.calendar


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eyeshield.expensetracker.MainNavRoutes
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.calendar.models.TransactionData
import com.eyeshield.expensetracker.database.DatabaseStatus
import com.eyeshield.expensetracker.home_graph.compose.home.TransactionDetails
import com.eyeshield.expensetracker.home_graph.compose.home.TransactionDetailsShimmer

@Composable
@Preview(showBackground = true)
fun CalendarScreen(
    onAddTransaction: ((TransactionData) -> Unit)? = null,
    getAllTransactions: List<TransactionData>? = listOf(),
    databaseStatus: DatabaseStatus = DatabaseStatus.LOADING,
    mainNavController: NavController = rememberNavController()
) {

    val items = remember(getAllTransactions) {
        mutableStateListOf<TransactionData>().apply {
            if (getAllTransactions?.isNotEmpty() == true)
                this.add(getAllTransactions[0])
            else {
                getAllTransactions?.map {
                    this.add(it)
                }
            }
        }
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            if (databaseStatus == DatabaseStatus.LOADING) {
                item {
                    TransactionDetailsShimmer()
                }
            } else {
                items(items.size.coerceAtMost(1), key = { it }) {
                    TransactionDetails(
                        transactionData = items[it], modifier = Modifier
                    )
                }
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
                mainNavController.navigate(MainNavRoutes.AddExpenseScreen.route)
            }) {
            Text(
                text = "Record a Expense", style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 16.sp,
                    color = Color.White
                )
            )
        }
    }


}

fun Modifier.shimmerLoadingAnimation(
    durationMillis: Int = 1000,
): Modifier {
    return composed {

        var size by remember { mutableStateOf(IntSize.Zero) }
        val shimmerColors = listOf(
            Color(0xFFB8B5B5),
            Color(0xFF8F8B8B),
            Color(0xFFB8B5B5)
        )

        val transition = rememberInfiniteTransition(label = "")

        val startOffsetX by transition.animateFloat(
            initialValue = -2 * size.width.toFloat(),
            targetValue = 2 * size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Shimmer loading animation",
        )

        this
            .background(
                brush = Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset(x = startOffsetX, y = 0f),
                    end = Offset(
                        x = startOffsetX + size.width.toFloat(),
                        y = size.height.toFloat()
                    ),
                ),
            )
            .onGloballyPositioned {
                size = it.size
            }
    }
}
