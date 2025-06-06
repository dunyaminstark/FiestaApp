package com.dunyamin.fiestaapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dunyamin.fiestaapp.R
import com.dunyamin.fiestaapp.data.Flag

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onLogoClick: () -> Unit = {}
) {
    val initialFlag = remember { Flag("tr", "Turkey", R.drawable.tr) }
    var selectedFlag by remember { mutableStateOf<Flag>(initialFlag) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 8.dp, end = 8.dp), // Reduced top padding, added horizontal padding
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // your logo here
            contentDescription = "Logo",
            modifier = Modifier
                .size(48.dp) // Increased size for better visibility and touch target
                .padding(4.dp) // Added padding for better visual appearance
                .clickable { onLogoClick() }
        )
        FlagSelector(
            initialFlag = selectedFlag,
            onFlagSelected = { flag ->
                selectedFlag = flag
            }
        )
    }
}
