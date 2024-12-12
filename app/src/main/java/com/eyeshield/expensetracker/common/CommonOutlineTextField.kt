package com.eyeshield.expensetracker.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R

@Composable
fun CommonOutlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    startIcon: ImageVector?,
    endIcon: @Composable () -> Unit?,
    hint: String,
    title: String?,
    modifier: Modifier = Modifier,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    additionalInfo: String? = null,
    internalPaddingValues: PaddingValues = PaddingValues(12.dp)
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            title?.let {
                Text(
                    modifier = Modifier.padding(start = 5.dp, bottom = 2.dp),
                    text = title,
                    color = colorResource(R.color.white),
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 14.sp
                )
            }

            error?.let {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            } ?: additionalInfo?.let {
                Text(
                    text = additionalInfo,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        BasicTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isFocused) {
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.05f
                        )
                    } else {
                        colorResource(R.color.text_field_background)
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (isFocused) colorResource(R.color.white) else colorResource(R.color.text_field_unfocused_border),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(internalPaddingValues)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(
                color = colorResource(R.color.white),
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 16.sp,
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            cursorBrush = SolidColor(Color.White),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    startIcon?.let {
                        Icon(
                            imageVector = startIcon,
                            contentDescription = null,
                            tint = Color.Unspecified
                        )

                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (value.isEmpty() && !isFocused) {
                            Text(
                                text = hint,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                                fontSize = 16.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        innerTextField()
                    }

                    endIcon()
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF333131)
fun PreviewCommonOutlineTextField() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        CommonOutlineTextField(
            value = "",
            onValueChange = {

            },
            endIcon = {
                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    imageVector = CheckIcon,
                    contentDescription = null,
                    tint = Color.Unspecified
                )

            },
            hint = "test@example.com",
            startIcon = EmailIcon,
            title = "Email"
        )
    }
}