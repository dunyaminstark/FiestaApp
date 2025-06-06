package com.dunyamin.fiestaapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dunyamin.fiestaapp.data.Holiday

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HolidaySwiper(
    modifier: Modifier = Modifier,
    holidays: List<Holiday>,
    onEditHoliday: (Holiday) -> Unit = {},
    onUpdateHoliday: (Holiday) -> Unit = {}
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { holidays.size })

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxHeight(1f)  // Take up 100% of available height for a true fullscreen effect
    ) { page ->
        HolidayCard(
            holiday = holidays[page],
            onEditHoliday = onEditHoliday,
            onUpdateHoliday = onUpdateHoliday
        )
    }
}
