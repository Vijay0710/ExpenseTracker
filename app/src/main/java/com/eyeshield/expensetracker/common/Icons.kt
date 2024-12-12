package com.eyeshield.expensetracker.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.eyeshield.expensetracker.R

val CheckIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(
        R.drawable.check
    )

val EmailIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.email)

val LockIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.lock)

val EyeOpened: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.eye_opened)

val EyeClosed: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.eye_closed)

val AlertIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_alert)