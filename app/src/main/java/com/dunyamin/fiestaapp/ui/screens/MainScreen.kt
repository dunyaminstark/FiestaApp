package com.dunyamin.fiestaapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dunyamin.fiestaapp.R
import com.dunyamin.fiestaapp.data.Holiday
import com.dunyamin.fiestaapp.data.repository.HolidayRepositoryProvider
import com.dunyamin.fiestaapp.ui.components.EditHolidayDialog
import com.dunyamin.fiestaapp.ui.components.HolidaySwiper
import com.dunyamin.fiestaapp.ui.components.ProfileDrawerContent
import com.dunyamin.fiestaapp.ui.components.TopBar
import kotlinx.coroutines.launch
import java.time.Year

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

    // Get the HolidayRepository instance
    val holidayRepository = HolidayRepositoryProvider.rememberHolidayRepository()

    // Define default holiday names and images
    val holidayNames = listOf("Republic Day", "National Sovereignty", "Victory Day")
    val holidayImages = listOf(R.drawable.card_image, R.drawable.card_image2, R.drawable.card_image3)

    // Get the current year
    val currentYear = Year.now().value

    // State to hold the holidays
    var holidays by remember { mutableStateOf<List<Holiday>>(emptyList()) }

    // Load holidays from the repository
    fun loadHolidays() {
        holidays = holidayRepository.getHolidaysForYear(
            year = currentYear,
            defaultNames = holidayNames,
            defaultImageResIds = holidayImages
        )
    }

    // Effect to load holidays when the component is first composed
    // and reload them when the app is restarted
    DisposableEffect(Unit) {  // Using Unit as a key ensures this runs on every composition
        println("[DEBUG_LOG] Loading holidays in DisposableEffect")
        loadHolidays()
        onDispose { }
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
            Box(
                modifier = Modifier
                    .fillMaxSize()  // Take up the entire screen
                    .padding(innerPadding) // Apply Scaffold's padding
                    .background(Color(0xFFF0F0F0))
                    .padding(0.dp)  // Remove padding for a true fullscreen effect
            ) {
                // HolidaySwiper fills the entire screen
                HolidaySwiper(
                    holidays = holidays,
                    onEditHoliday = { holiday ->
                        holidayToEdit = holiday
                    },
                    onUpdateHoliday = { updatedHoliday ->
                        // Save the updated holiday using the repository
                        holidayRepository.saveHoliday(updatedHoliday)

                        // Reload holidays from the repository to ensure we have the latest data
                        loadHolidays()

                        // Show Snackbar
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Holiday date updated successfully!",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // TopBar is positioned at the top, overlaying the card
                TopBar(
                    onLogoClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    modifier = Modifier.align(Alignment.TopCenter)
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
                // Save the updated holiday using the repository
                holidayRepository.saveHoliday(updatedHoliday)

                // Reload holidays from the repository to ensure we have the latest data
                loadHolidays()

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
