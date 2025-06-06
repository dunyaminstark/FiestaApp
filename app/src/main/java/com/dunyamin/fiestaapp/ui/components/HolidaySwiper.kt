package com.dunyamin.fiestaapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dunyamin.fiestaapp.data.Holiday

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HolidaySwiper(
    holidays: List<Holiday>,
    onEditHoliday: (Holiday) -> Unit = {}
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { holidays.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(0.9f)  // Take up 90% of available height for fullscreen effect
    ) { page ->
        HolidayCard(
            holiday = holidays[page],
            onEditHoliday = onEditHoliday
        )
    }
}
