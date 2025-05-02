package com.eyeshield.expensetracker.calendar_graph.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.calendar_graph.TransactionViewModel
import com.eyeshield.expensetracker.calendar_graph.data.CalendarData
import com.eyeshield.expensetracker.extensions.bottomPadding
import com.eyeshield.expensetracker.extensions.topPadding
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun PaymentReminderCalendar(
    modifier: Modifier = Modifier,
    calendarData: CalendarData,
    uiAction: (TransactionViewModel.UiAction) -> Unit
) {

    val pagerState = rememberPagerState(
        initialPage = calendarData.monthIndex,
        pageCount = { 12 }
    )
    val scope = rememberCoroutineScope()
    var currentPage by rememberSaveable { mutableIntStateOf(pagerState.currentPage) }

    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.distinctUntilChanged().collect { page ->

            if (currentPage < page) {
                uiAction(
                    TransactionViewModel.UiAction.OnRightChevronClicked(currentPage)
                )
            }

            if (currentPage > page) {
                uiAction(
                    TransactionViewModel.UiAction.OnLeftChevronClicked(currentPage)
                )
            }

            currentPage = page
        }
    }

    Column(
        modifier = modifier.topPadding(20.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White,
            shadowElevation = 10.dp,
            shape = RoundedCornerShape(30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = calendarData.monthAndYear, style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                            fontWeight = FontWeight.ExtraBold,
                            color = colorResource(id = R.color.carbon_blue)
                        )
                    )
                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = pagerState.currentPage - 1,
                                    animationSpec = tween(600, easing = LinearOutSlowInEasing),
                                )
                            }
                            uiAction(
                                TransactionViewModel.UiAction.OnLeftChevronClicked(currentPage)
                            )
                            currentPage -= 1
                        },
                        enabled = currentPage > 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Left Chevron",
                            tint = if (currentPage > 1)
                                colorResource(id = R.color.carbon_blue)
                            else
                                colorResource(id = R.color.carbon_blue).copy(0.5f)
                        )
                    }

                    IconButton(
                        onClick = {
                            scope.launch {
                                if (pagerState.pageCount != pagerState.currentPage) {
                                    pagerState.animateScrollToPage(
                                        page = pagerState.currentPage + 1,
                                        animationSpec = tween(600, easing = LinearOutSlowInEasing)
                                    )
                                }
                            }
                            uiAction(
                                TransactionViewModel.UiAction.OnRightChevronClicked(currentPage)
                            )
                            currentPage += 1
                        },
                        enabled = currentPage != pagerState.pageCount
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Right Chevron",
                            tint =
                                if (currentPage != pagerState.pageCount)
                                    colorResource(id = R.color.carbon_blue)
                                else
                                    colorResource(id = R.color.carbon_blue).copy(0.5f)
                        )
                    }
                }

                CustomCalendar(
                    pagerState,
                    calendarData,
                    uiAction
                )
            }
        }
    }


}

@Composable
fun CustomCalendar(
    horizontalPagerState: PagerState,
    calendarData: CalendarData,
    uiAction: (TransactionViewModel.UiAction) -> Unit
) {

    val weekAbbreviationList = remember {
        listOf("S", "M", "T", "W", "T", "F", "S")
    }

    val state = rememberLazyGridState()


    val selectedDayColor = colorResource(id = R.color.selected_day)

    val todayBackgroundModifier = remember {
        Modifier.drawBehind {
            drawCircle(selectedDayColor, radius = 55f)
        }
    }

    val selectedDayBackgroundModifier = remember {
        Modifier.drawBehind {
            drawCircle(selectedDayColor, radius = 50f, style = Stroke(width = 4f))
            drawCircle(selectedDayColor.copy(0.1f), radius = 50f)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .topPadding(12.dp)
            .bottomPadding(12.dp)
    ) {
        weekAbbreviationList.forEach { week ->
            key(week) {
                Text(
                    modifier = Modifier.weight(1f), text = week, style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.carbon_blue),
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }

    HorizontalPager(state = horizontalPagerState, userScrollEnabled = true) {
        LazyVerticalGrid(
            modifier = Modifier
                .height(220.dp),
            state = state,
            columns = GridCells.Fixed(7),
            userScrollEnabled = true,
        ) {
            // Empty set of items to indicate the start day offset in the month
            items(calendarData.dayStartingColumn) {}

            items(calendarData.totalDays) { item ->
                key(horizontalPagerState, item) {
                    val interactionSource = remember { MutableInteractionSource() }
                    Text(
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = ripple(bounded = false, radius = 15.dp),
                                onClick = remember {
                                    {
                                        uiAction(
                                            TransactionViewModel.UiAction.UpdateSelectedDay(
                                                item + 1
                                            )
                                        )
                                    }
                                }
                            )
                            .then(
                                if (!horizontalPagerState.isScrollInProgress && calendarData.currentDay == item + 1 && calendarData.isCurrentMonth)
                                    todayBackgroundModifier
                                else if (calendarData.selectedDay != calendarData.currentDay && calendarData.selectedDay == item + 1)
                                    selectedDayBackgroundModifier
                                else
                                    Modifier
                            ),
                        text = buildAnnotatedString {
                            append("${item + 1}")
                        },
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 14.sp,
                            color =
                                if (!horizontalPagerState.isScrollInProgress && calendarData.currentDay == item + 1 && calendarData.isCurrentMonth)
                                    Color.White
                                else
                                    colorResource(id = R.color.carbon_blue),
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }

}
