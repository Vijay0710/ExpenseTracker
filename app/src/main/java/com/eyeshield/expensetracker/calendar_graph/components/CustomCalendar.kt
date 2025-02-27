package com.eyeshield.expensetracker.calendar_graph.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eyeshield.expensetracker.R
import com.eyeshield.expensetracker.calendar_graph.data.CalendarData
import com.eyeshield.expensetracker.utils.CalendarUtils
import kotlinx.coroutines.launch

@Composable
@Preview
fun PaymentReminderCalendar(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 12 }
    )
    val scope = rememberCoroutineScope()
    val (currentMonth, setCurrentMonth) = remember {
        mutableIntStateOf(CalendarUtils.getCurrentMonth())
    }
    var currentPage by remember {
        mutableIntStateOf(pagerState.currentPage)
    }
    val monthAndYear = remember { mutableStateOf(CalendarUtils.getMonthAndYear(currentMonth)) }

    LaunchedEffect(key1 = currentMonth) {
        monthAndYear.value = CalendarUtils.getMonthAndYear(currentMonth)
    }

    Column(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White,
            shadowElevation = 12.dp,
            tonalElevation = 32.dp,
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
                        text = monthAndYear.value, style = TextStyle(
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
                            currentPage -= 1
                            setCurrentMonth(currentMonth - 1)
                        },
                        enabled = currentPage > 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Left Icon",
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
                            setCurrentMonth(currentMonth + 1)
                            currentPage += 1
                        },
                        enabled = currentPage != pagerState.pageCount
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Left Icon",
                            tint =
                            if (currentPage != pagerState.pageCount)
                                colorResource(id = R.color.carbon_blue)
                            else
                                colorResource(id = R.color.carbon_blue).copy(0.5f)
                        )
                    }
                }

                CustomCalendar(pagerState, currentMonth)
            }
        }
    }


}

@Composable
fun CustomCalendar(
    pagerState: PagerState,
    updatedMonthPosition: Int,
) {

    val weekAbbreviationList = remember {
        listOf("S", "M", "T", "W", "T", "F", "S")
    }

    val state = rememberLazyGridState()
    val horizontalPagerState = remember(pagerState) {
        pagerState
    }

    var calendarData by remember {
        mutableStateOf(
            CalendarData(
                currentMonthPosition = updatedMonthPosition,
                dayStartingColumn = CalendarUtils.getStartDayOfTheMonthOffset(updatedMonthPosition),
                currentDay = CalendarUtils.getCurrentDay(),
                totalDays = CalendarUtils.getTotalDaysForCurrentMonth(updatedMonthPosition),
                selectedDay = 0
            )
        )
    }

    val selectedDayColor = colorResource(id = R.color.selected_day)

    LaunchedEffect(key1 = updatedMonthPosition) {
        calendarData = calendarData.copy(
            dayStartingColumn = CalendarUtils.getStartDayOfTheMonthOffset(updatedMonthPosition),
            currentDay = CalendarUtils.getCurrentDay(),
            totalDays = CalendarUtils.getTotalDaysForCurrentMonth(updatedMonthPosition),
            selectedDay = 0
        )
    }

    val todayBackgroundModifier = remember {
        Modifier.drawBehind {
            drawCircle(selectedDayColor, radius = 38f)
        }
    }

    val selectedDayBackgroundModifier = remember {
        Modifier.drawBehind {
            drawCircle(selectedDayColor, radius = 38f, style = Stroke(width = 4f))
            drawCircle(selectedDayColor.copy(0.1f), radius = 38f)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 12.dp)
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

    HorizontalPager(state = horizontalPagerState, userScrollEnabled = false) {
        LazyVerticalGrid(
            state = state,
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = true,
        ) {
            // Empty set of items to indicate the start day offset in the month
            items(calendarData.dayStartingColumn) {}

            items(calendarData.totalDays) { item ->
                key(horizontalPagerState, item) {
                    val interactionSource = remember { MutableInteractionSource() }
                    Text(
                        modifier = Modifier
                            .padding(7.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = ripple(bounded = false, radius = 15.dp),
                                onClick = remember {
                                    {
                                        calendarData = calendarData.copy(selectedDay = item + 1)
                                    }
                                }
                            )
                            .then(
                                if (!horizontalPagerState.isScrollInProgress && calendarData.currentDay == item + 1 && calendarData.currentMonthPosition == updatedMonthPosition)
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
                            if (!horizontalPagerState.isScrollInProgress && calendarData.currentDay == item + 1 && calendarData.currentMonthPosition == updatedMonthPosition)
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
