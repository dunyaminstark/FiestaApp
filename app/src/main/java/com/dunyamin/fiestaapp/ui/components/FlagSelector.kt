package com.dunyamin.fiestaapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dunyamin.fiestaapp.R
import com.dunyamin.fiestaapp.data.Flag

@Composable
fun FlagSelector(
    modifier: Modifier = Modifier,
    initialFlag: Flag,
    onFlagSelected: (Flag) -> Unit
) {
    val flags = listOf(
        Flag("tr", "Turkey", R.drawable.tr),
        Flag("gb", "United Kingdom", R.drawable.gb),
        Flag("de", "Germany", R.drawable.de),
        Flag("es", "Spain", R.drawable.es),
        Flag("ca", "Canada", R.drawable.ca)
    )
    
    var selectedFlag by remember { mutableStateOf(initialFlag) }
    var showDialog by remember { mutableStateOf(false) }
    
    // Flag preview that opens the dialog when clicked
    Image(
        painter = painterResource(id = selectedFlag.flagResource),
        contentDescription = "${selectedFlag.countryName} flag",
        modifier = modifier
            .size(40.dp)
            .clickable { showDialog = true }
    )
    
    // Dialog with flag options
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Select Country",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    LazyColumn {
                        items(flags) { flag ->
                            FlagItem(
                                flag = flag,
                                isSelected = flag.countryCode == selectedFlag.countryCode,
                                onClick = {
                                    selectedFlag = flag
                                    onFlagSelected(flag)
                                    showDialog = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FlagItem(
    flag: Flag,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFE0E0E0) else Color.Transparent
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = flag.flagResource),
            contentDescription = "${flag.countryName} flag",
            modifier = Modifier.size(32.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(text = flag.countryName)
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}