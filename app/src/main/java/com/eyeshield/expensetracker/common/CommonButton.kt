package com.eyeshield.expensetracker.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R

@Composable
fun CommonButton(
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    shouldShowLoader: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        onClick = onClick,
        shape = RoundedCornerShape(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.button_container_color),
            disabledContainerColor = colorResource(R.color.button_container_color),
            contentColor = colorResource(R.color.button_content_color),
            disabledContentColor = colorResource(R.color.button_content_color)
        ),
        enabled = enabled
    ) {
        if (shouldShowLoader) {
            DotsPulsing()
        } else {
            Text(
                modifier = Modifier,
                text = title,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W800
                )
            )
        }
    }
}

@Preview(widthDp = 200)
@Composable
private fun PreviewCommonButton() {
    CommonButton(
        title = "Login",
        onClick = {

        },
        shouldShowLoader = false
    )
}