package com.eyeshield.expensetracker.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun CalendarDesign() {
    val pagerState = rememberPagerState(pageCount = {
        10
    })
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        color = Color.White,
        shadowElevation = 32.dp,
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
                    text = "March 2024", style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(id = R.color.carbon_blue)
                    )
                )
                IconButton(onClick = {
                    scope.launch {
                        pagerState.scrollToPage(pagerState.currentPage - 1)
                    }
                }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Left Icon", tint = colorResource(
                        id = R.color.carbon_blue
                    ))
                }

                IconButton(onClick = {
                    scope.launch {
                        pagerState.scrollToPage(pagerState.currentPage + 1)
                    }
                }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Left Icon", tint = colorResource(
                        id = R.color.carbon_blue
                    ))
                }
            }

            WeekDayAbbreviationBar(pagerState)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeekDayAbbreviationBar(pagerState: PagerState) {

    val weekAbbreviationList = remember {
        listOf("S", "M", "T", "W", "T", "F", "S")
    }

    val state = rememberLazyGridState()


    val selectedDayColor = colorResource(id = R.color.selected_day)
    val dayStartingColumn = remember { mutableIntStateOf(6) }
    val selectedDay = remember { mutableIntStateOf(31) }

    val startPosition = remember {
        derivedStateOf {
            if (dayStartingColumn.intValue - pagerState.currentPage >= 0)
                dayStartingColumn.intValue - pagerState.currentPage
            else
                0
        }
    }

    val selectedBackgroundModifier = remember {
        Modifier
            .drawBehind {
                drawCircle(selectedDayColor, radius = 38f)
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

    HorizontalPager(state = pagerState) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Empty set of items to indicate the start day offset in the month
            items(startPosition.value) {}

            items(31) { item ->
                ClickableText(
                    modifier = Modifier
                        .padding(5.dp)
                        .then(if (selectedDay.intValue == item + 1) selectedBackgroundModifier else Modifier),
                    text = buildAnnotatedString { append((item + 1).toString()) },
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        fontSize = 14.sp,
                        color = if (selectedDay.intValue == item + 1) Color.White else colorResource(
                            id = R.color.carbon_blue
                        ),
                        textAlign = TextAlign.Center
                    ),
                    onClick = {
                        selectedDay.intValue = item + 1
                    }
                )
            }
        }
    }


}
