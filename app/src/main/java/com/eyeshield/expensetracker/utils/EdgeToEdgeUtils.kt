package com.eyeshield.expensetracker.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eyeshield.expensetracker.common.block
import com.eyeshield.expensetracker.extensions.bottomPadding
import com.eyeshield.expensetracker.extensions.endPadding
import com.eyeshield.expensetracker.extensions.startPadding
import com.eyeshield.expensetracker.extensions.topPadding

@Composable
private fun PaddingValues.rightPadding(): Dp = calculateRightPadding(LocalLayoutDirection.current)

@Composable
private fun PaddingValues.leftPadding(): Dp = calculateLeftPadding(LocalLayoutDirection.current)

@Composable
fun Modifier.handleEdgeToEdgeInsets(
    isScaffold: Boolean = true,
    shouldHandleStatusBarInsets: Boolean = false,
    shouldHandleBottomBarInsets: Boolean = false
): Modifier {

    val cameraInsets = WindowInsets.displayCutout.asPaddingValues()
    val statusBarInsets = WindowInsets.statusBars.asPaddingValues()
    val navigationBarInsets = WindowInsets.navigationBars.asPaddingValues()

    val leftCameraPadding = cameraInsets.leftPadding()
    val rightCameraPadding = cameraInsets.rightPadding()

    val leftNavigationPadding = navigationBarInsets.leftPadding()
    val rightNavigationPadding = navigationBarInsets.rightPadding()

    val statusBarPadding =
        if (shouldHandleStatusBarInsets) statusBarInsets.calculateTopPadding() else 0.dp
    val bottomBarPadding =
        if (shouldHandleBottomBarInsets) navigationBarInsets.calculateBottomPadding() else 0.dp

    return block {
        if (isScaffold) {
            Modifier
                .startPadding(leftCameraPadding)
                .endPadding(rightCameraPadding)
        } else {
            Modifier
                .startPadding(leftNavigationPadding + leftCameraPadding)
                .endPadding(rightNavigationPadding + rightCameraPadding)
                .topPadding(statusBarPadding)
                .bottomPadding(bottomBarPadding)
        }
    }
}