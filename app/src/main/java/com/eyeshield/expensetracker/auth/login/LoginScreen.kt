package com.eyeshield.expensetracker.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.common.AnimatedToast
import com.eyeshield.expensetracker.common.CheckIcon
import com.eyeshield.expensetracker.common.CommonButton
import com.eyeshield.expensetracker.common.CommonOutlineTextField
import com.eyeshield.expensetracker.common.EmailIcon
import com.eyeshield.expensetracker.common.EyeClosed
import com.eyeshield.expensetracker.common.EyeOpened
import com.eyeshield.expensetracker.common.GradientBackground
import com.eyeshield.expensetracker.common.LockIcon
import com.eyeshield.expensetracker.extensions.horizontalPadding
import com.eyeshield.expensetracker.extensions.topPadding

@Composable
fun LoginScreen(
    uiState: LoginViewModel.LoginState,
    uiAction: (LoginViewModel.UiAction) -> Unit
) {

    GradientBackground {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp)
                    .shadow(
                        elevation = 12.dp,
                        clip = false,
                        shape = RoundedCornerShape(
                            topStart = 70.dp,
                            topEnd = 70.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 1.dp
                        ),
                        spotColor = colorResource(R.color.login_surface_ambient_shadow_color).copy(
                            0.1f
                        )
                    ),
                shape = RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp),
                color = colorResource(R.color.login_surface_background).copy(0.6f),
                border = BorderStroke(
                    1.dp,
                    color = colorResource(R.color.login_surface_stroke).copy(0.3f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.login_title),
                        style = TextStyle(
                            fontFamily = FontFamily(Font((R.font.nunito_bold))),
                            fontSize = 20.sp,
                            color = colorResource(R.color.white)
                        )
                    )

                    Spacer(modifier = Modifier.topPadding(40.dp))

                    CommonOutlineTextField(
                        modifier = Modifier.topPadding(30.dp),
                        value = uiState.email,
                        onValueChange = {
                            uiAction(LoginViewModel.UiAction.OnEmailValueChange(it))
                        },
                        startIcon = EmailIcon,
                        endIcon = {
                            if (uiState.isEmailValid) {
                                Spacer(modifier = Modifier.width(16.dp))

                                Icon(
                                    modifier = Modifier.padding(end = 8.dp),
                                    imageVector = CheckIcon,
                                    contentDescription = null,
                                    tint = Color.Unspecified
                                )
                            }
                        },
                        hint = stringResource(R.string.email_text_field_hint),
                        title = stringResource(R.string.email_text_field_title)
                    )

                    CommonOutlineTextField(
                        modifier = Modifier.topPadding(30.dp),
                        value = uiState.password,
                        onValueChange = {
                            uiAction(LoginViewModel.UiAction.OnPasswordValueChange(it))
                        },
                        startIcon = LockIcon,
                        endIcon = {
                            IconButton(
                                onClick = {
                                    uiAction(LoginViewModel.UiAction.OnTogglePasswordVisibility(!uiState.isPasswordVisible))
                                }
                            ) {
                                Row {
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Icon(
                                        modifier = Modifier,
                                        imageVector = if (uiState.isPasswordVisible) EyeClosed else EyeOpened,
                                        contentDescription = null,
                                        tint = Color.Unspecified
                                    )
                                }
                            }
                        },
                        hint = stringResource(R.string.password_text_field_hint),
                        title = stringResource(R.string.password_text_field_title),
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        internalPaddingValues = PaddingValues(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.topPadding(40.dp))

                    CommonButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .horizontalPadding(50.dp),
                        title = stringResource(R.string.login_button_title),
                        enabled = !uiState.shouldShowLoader,
                        shouldShowLoader = uiState.shouldShowLoader,
                        onClick = {
                            uiAction(LoginViewModel.UiAction.OnLoginClick)
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    AnimatedToast(
        shouldShowToast = uiState.shouldShowToast,
        errorMessage = uiState.errorMessage,
        onProgressFinished = {
            uiAction.invoke(LoginViewModel.UiAction.OnTrackAnimationFinished)
        }
    )
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    LoginScreen(
        uiState = LoginViewModel.LoginState(
            email = "vijaymangalani4588@gmail.com",
            password = "abcdef",
            isPasswordVisible = false,
            shouldShowLoader = false,
            shouldShowToast = true,
            errorMessage = "Something went wrong on our end! Our team is on it, armed with coffee and determination!"
        ),
        uiAction = {}
    )
}