package com.dunyamin.fiestaapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dunyamin.fiestaapp.data.Holiday
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.dunyamin.fiestaapp.ui.components.HolidayImage

@Composable
fun HolidayCard(
    holiday: Holiday,
    onEditHoliday: (Holiday) -> Unit = {}
) {
    // State to track if the date picker should be shown
    var showDatePicker by remember { mutableStateOf(false) }
    // State to store the selected date
    var selectedDate by remember { mutableStateOf<Date?>(null) }

    // Format for displaying the date
    val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())

    // Determine the text to display for the date
    val displayDate = if (selectedDate != null) {
        dateFormat.format(selectedDate!!)
    } else {
        holiday.date
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),  // Fill the entire available space
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Fullscreen image
            HolidayImage(
                imageRes = holiday.imageRes,
                customImageUri = holiday.customImageUri,
                contentDescription = holiday.name,
                modifier = Modifier.fillMaxSize()
            )

            // Text container at the bottom center
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Holiday name with a semi-transparent background - now clickable
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .shadow(4.dp, RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .clickable { onEditHoliday(holiday) }  // Make the box clickable to edit holiday
                ) {
                    Text(
                        text = holiday.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Holiday date with a semi-transparent background – now clickable
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .shadow(4.dp, RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .clickable { showDatePicker = true }  // Make the box clickable
                ) {
                    Text(
                        text = displayDate,
                        fontSize = 20.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }

    // Show the date picker dialog when requested
    if (showDatePicker) {
        ShowDatePickerDialog(
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}
