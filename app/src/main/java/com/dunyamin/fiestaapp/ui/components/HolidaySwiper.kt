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
    onEditHoliday: (Holiday) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { holidays.size })

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxHeight(1f)  // Take up 100% of available height for true fullscreen effect
    ) { page ->
        HolidayCard(
            holiday = holidays[page],
            onEditHoliday = onEditHoliday
        )
    }
}
