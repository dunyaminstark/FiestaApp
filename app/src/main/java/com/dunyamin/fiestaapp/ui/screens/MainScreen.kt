package com.dunyamin.fiestaapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dunyamin.fiestaapp.R
import com.dunyamin.fiestaapp.data.Holiday
import com.dunyamin.fiestaapp.data.HolidayPreferences
import com.dunyamin.fiestaapp.data.rememberHolidayPreferences
import com.dunyamin.fiestaapp.ui.components.EditHolidayDialog
import com.dunyamin.fiestaapp.ui.components.HolidaySwiper
import com.dunyamin.fiestaapp.ui.components.ProfileDrawerContent
import com.dunyamin.fiestaapp.ui.components.TopBar
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter

/**
 * Generates a list of holidays for an entire year, cycling through the provided names and images.
 * 
 * @param names List of holiday names to cycle through
 * @param imageResIds List of image resource IDs to cycle through
 * @param dateFormatter Formatter to format the date strings
 * @param holidayPreferences HolidayPreferences instance to load saved holiday names
 * @return List of Holiday objects, one for each day of the year
 */
private fun generateYearOfHolidays(
    names: List<String>,
    imageResIds: List<Int>,
    dateFormatter: DateTimeFormatter,
    holidayPreferences: HolidayPreferences
): List<Holiday> {
    val currentYear = Year.now().value
    val startDate = LocalDate.of(currentYear, 1, 1)
    val isLeapYear = Year.isLeap(currentYear.toLong())
    val daysInYear = if (isLeapYear) 366 else 365

    return List(daysInYear) { dayOffset ->
        val date = startDate.plusDays(dayOffset.toLong())
        val nameIndex = dayOffset % names.size
        val imageIndex = dayOffset % imageResIds.size
        val formattedDate = date.format(dateFormatter)

        // Use saved holiday name if available, otherwise use default
        val defaultName = names[nameIndex]
        val holidayName = holidayPreferences.getHolidayName(formattedDate, defaultName)

        // Use saved image resource ID if available, otherwise use default
        val defaultImageRes = imageResIds[imageIndex]
        val imageRes = holidayPreferences.getHolidayImageRes(formattedDate, defaultImageRes)

        // Check if there is a custom image for this date
        val customImageUri = holidayPreferences.getCustomHolidayImage(formattedDate)

        Holiday(
            name = holidayName,
            date = formattedDate,
            imageRes = imageRes,
            customImageUri = customImageUri
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    onUpdateInfoClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onSignupClick: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Get the HolidayPreferences instance
    val holidayPreferences = rememberHolidayPreferences()

    // Generate 365 holidays, one for each day of the year
    val holidayNames = listOf("Republic Day", "National Sovereignty", "Victory Day")
    val holidayImages = listOf(R.drawable.card_image, R.drawable.card_image2, R.drawable.card_image3)
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM")

    // Generate a list of 365 holidays (or 366 for leap years) and convert to mutable state
    var holidays by remember { 
        mutableStateOf(generateYearOfHolidays(holidayNames, holidayImages, dateFormatter, holidayPreferences)) 
    }

    // State to track the currently selected holiday for editing
    var holidayToEdit by remember { mutableStateOf<Holiday?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                ProfileDrawerContent(
                    onUpdateInfoClick = onUpdateInfoClick,
                    onLoginClick = onLoginClick,
                    onSignupClick = onSignupClick
                )
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()  // Take up the entire screen
                    .padding(innerPadding) // Apply Scaffold's padding
                    .background(Color(0xFFF0F0F0))
                    .padding(8.dp)  // Reduced padding for a more fullscreen effect
            ) {
                TopBar(
                    onLogoClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                HolidaySwiper(
                    holidays = holidays,
                    onEditHoliday = { holiday ->
                        holidayToEdit = holiday
                    }
                )
            }
        }
    }

    // Show the edit holiday dialog when a holiday is selected for editing
    holidayToEdit?.let { holiday ->
        EditHolidayDialog(
            holiday = holiday,
            onDismiss = { holidayToEdit = null },
            onSave = { updatedHoliday ->
                // Save the updated holiday name to SharedPreferences
                holidayPreferences.saveHolidayName(updatedHoliday.date, updatedHoliday.name)

                // Save the updated image resource ID to SharedPreferences
                holidayPreferences.saveHolidayImageRes(updatedHoliday.date, updatedHoliday.imageRes)

                // Save the custom image URI if it exists
                if (updatedHoliday.hasCustomImage()) {
                    holidayPreferences.saveCustomHolidayImage(updatedHoliday.date, updatedHoliday.customImageUri!!)
                }

                // Update the holiday in the list
                holidays = holidays.map { h ->
                    if (h.date == updatedHoliday.date) {
                        updatedHoliday
                    } else {
                        h
                    }
                }
                // Close the dialog
                holidayToEdit = null

                // Show Snackbar
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Holiday updated successfully!",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }
}
