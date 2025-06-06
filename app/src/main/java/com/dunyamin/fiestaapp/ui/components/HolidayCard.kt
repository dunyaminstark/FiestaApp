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
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HolidayCard(
    holiday: Holiday,
    onEditHoliday: (Holiday) -> Unit = {},
    onUpdateHoliday: (Holiday) -> Unit = {}
) {
    // State to track if the date picker should be shown
    var showDatePicker by remember { mutableStateOf(false) }

    // Format for displaying the date
    val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())

    // Use the holiday date directly for display
    val displayDate = holiday.date

    Card(
        shape = RoundedCornerShape(0.dp), // Remove rounded corners for full-screen effect
        modifier = Modifier
            .padding(0.dp) // Remove padding for true fullscreen
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
                // Holiday name with a more opaque background for better readability - now clickable
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .shadow(6.dp, RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                        .clickable { onEditHoliday(holiday) }  // Make the box clickable to edit holiday
                ) {
                    Text(
                        text = holiday.name,
                        fontSize = 30.sp, // Increased font size for better readability
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 12.dp) // Increased padding for better touch target
                    )
                }

                // Holiday date with a more opaque background for better readability – now clickable
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .shadow(6.dp, RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                        .clickable { showDatePicker = true }  // Make the box clickable
                ) {
                    Text(
                        text = displayDate,
                        fontSize = 24.sp, // Increased font size for better readability
                        color = Color.Black, // Darker color for better contrast
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 12.dp) // Increased padding for better touch target
                    )
                }
            }
        }
    }

    // Show the date picker dialog when requested
    if (showDatePicker) {
        ShowDatePickerDialog(
            onDateSelected = { date ->
                // Format the selected date to match the repository's format
                val calendar = Calendar.getInstance()
                calendar.time = date
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())

                // Create a new Holiday with the updated date
                val updatedHoliday = Holiday(
                    name = holiday.name,
                    date = "$day $month",
                    imageRes = holiday.imageRes,
                    customImageUri = holiday.customImageUri
                )

                // Call the callback to update the holiday
                onUpdateHoliday(updatedHoliday)
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}
