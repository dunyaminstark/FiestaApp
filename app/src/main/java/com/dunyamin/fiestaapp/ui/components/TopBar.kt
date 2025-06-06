package com.dunyamin.fiestaapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dunyamin.fiestaapp.R
import com.dunyamin.fiestaapp.data.Flag

@Composable
fun TopBar(onLogoClick: () -> Unit = {}) {
    val initialFlag = remember { Flag("tr", "Turkey", R.drawable.tr) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // your logo here
            contentDescription = "Logo",
            modifier = Modifier
                .size(40.dp)
                .clickable { onLogoClick() }
        )
        FlagSelector(
            initialFlag = initialFlag,
            onFlagSelected = { /* TODO: Handle flag selection */ }
        )
    }
}
