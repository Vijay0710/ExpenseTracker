package com.eyeshield.expensetracker.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.home_graph.home.HomeScreen

@Preview(showBackground = true, backgroundColor = 0xFFF6F6F6)
@Composable
fun OpaqueLoaderScreen(
    isLoading: Boolean = true,
    content: (@Composable () -> Unit) = { HomeScreen(rememberNavController()) }
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isLoading) Color.Gray.copy(0.5f) else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        content.invoke()
        if (isLoading) {
            Box(
                modifier = Modifier
                    .shadow(32.dp)
                    .size(150.dp)
                    .background(Color.White, shape = RoundedCornerShape(10)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorResource(id = R.color.carbon_blue))
            }
        }
    }
}