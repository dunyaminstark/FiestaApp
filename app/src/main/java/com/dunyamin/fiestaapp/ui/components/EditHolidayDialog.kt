package com.dunyamin.fiestaapp.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dunyamin.fiestaapp.R
import com.dunyamin.fiestaapp.data.Holiday
import com.dunyamin.fiestaapp.ui.theme.Purple40
import com.dunyamin.fiestaapp.util.rememberImageStorageManager
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHolidayDialog(
    holiday: Holiday,
    onDismiss: () -> Unit,
    onSave: (Holiday) -> Unit
) {
    var holidayName by remember { mutableStateOf(holiday.name) }
    var holidayDate by remember { mutableStateOf(holiday.date) }
    var isError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Format for displaying the date
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM")

    // Get the ImageStorageManager instance
    val imageStorageManager = rememberImageStorageManager()

    // Available holiday images
    val holidayImages = listOf(
        R.drawable.card_image,
        R.drawable.card_image2,
        R.drawable.card_image3
    )

    // State to track the selected image
    var selectedImageRes by remember { mutableStateOf(holiday.imageRes) }

    // State to track the custom image URI
    var customImageUri by remember { mutableStateOf(holiday.customImageUri) }

    // State to track the original custom image URI (for cleanup)
    val originalCustomImageUri = remember { holiday.customImageUri }

    // Log the initial custom image URI
    println("[DEBUG_LOG] Initial custom image URI in EditHolidayDialog: $customImageUri")

    // Validate input and handle save action
    fun validateAndSave() {
        if (holidayName.trim().isEmpty()) {
            isError = true
        } else {
            println("[DEBUG_LOG] Saving holiday with custom image URI: $customImageUri")

            // If the original image is different from the current one and is an internal storage URI, delete it
            if (originalCustomImageUri != customImageUri && !originalCustomImageUri.isNullOrEmpty()) {
                try {
                    val originalUri = Uri.parse(originalCustomImageUri)
                    if (imageStorageManager.isInternalStorageUri(originalUri)) {
                        val deleted = imageStorageManager.deleteImageFromInternalStorage(originalUri)
                        println("[DEBUG_LOG] Original image deleted on save: $deleted")
                    }
                } catch (e: Exception) {
                    println("[DEBUG_LOG] Error deleting original image: ${e.message}")
                }
            }

            // Create a new Holiday with the updated name, date, and image
            val updatedHoliday = Holiday(
                name = holidayName.trim(),
                date = holidayDate,
                imageRes = selectedImageRes,
                customImageUri = customImageUri
            )

            println("[DEBUG_LOG] Created updated holiday: ${updatedHoliday.name}, ${updatedHoliday.date}, ${updatedHoliday.imageRes}, ${updatedHoliday.customImageUri}")

            onSave(updatedHoliday)
        }
    }

    // Function to clean up any new images that were added but not saved
    fun cleanupUnsavedImages() {
        // If the current custom image URI is different from the original and is an internal storage URI
        if (customImageUri != originalCustomImageUri && !customImageUri.isNullOrEmpty()) {
            try {
                val currentUri = Uri.parse(customImageUri)
                if (imageStorageManager.isInternalStorageUri(currentUri)) {
                    val deleted = imageStorageManager.deleteImageFromInternalStorage(currentUri)
                    println("[DEBUG_LOG] Unsaved image deleted on dismiss: $deleted")
                }
            } catch (e: Exception) {
                println("[DEBUG_LOG] Error deleting unsaved image: ${e.message}")
            }
        }

        // Call the original onDismiss callback
        onDismiss()
    }

    Dialog(onDismissRequest = { cleanupUnsavedImages() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = "Edit Holiday",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Purple40,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Holiday name field with icon and validation
                OutlinedTextField(
                    value = holidayName,
                    onValueChange = { 
                        holidayName = it
                        if (isError && it.trim().isNotEmpty()) {
                            isError = false
                        }
                    },
                    label = { Text("Holiday Name") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Holiday Name",
                            tint = Purple40
                        )
                    },
                    isError = isError,
                    supportingText = {
                        if (isError) {
                            Text(
                                text = "Holiday name cannot be empty",
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text("Enter a meaningful name for your holiday")
                        }
                    },
                    trailingIcon = {
                        if (isError) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            validateAndSave()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Holiday date field with icon
                OutlinedTextField(
                    value = holidayDate,
                    onValueChange = { /* Read-only field, handled by date picker */ },
                    label = { Text("Holiday Date") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Edit Holiday Date",
                            tint = Purple40
                        )
                    },
                    readOnly = true,
                    supportingText = {
                        Text("Click to select a date for your holiday")
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Image selection section
                Text(
                    text = "Holiday Image",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .align(Alignment.Start)
                )

                // Current selected image preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(bottom = 16.dp)
                        .shadow(2.dp, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    HolidayImage(
                        imageRes = selectedImageRes,
                        customImageUri = customImageUri,
                        contentDescription = "Selected Holiday Image",
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Overlay text
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Current Image",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Available images selection
                Text(
                    text = "Select a new image:",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .align(Alignment.Start)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    items(holidayImages) { imageRes ->
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .border(
                                    width = 2.dp,
                                    color = if (imageRes == selectedImageRes && customImageUri == null) Purple40 else Color.Transparent,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable { 
                                    selectedImageRes = imageRes

                                    // Delete the custom image if it exists and is an internal storage URI
                                    if (!customImageUri.isNullOrEmpty()) {
                                        try {
                                            val oldUri = Uri.parse(customImageUri)
                                            if (imageStorageManager.isInternalStorageUri(oldUri)) {
                                                val deleted = imageStorageManager.deleteImageFromInternalStorage(oldUri)
                                                println("[DEBUG_LOG] Custom image deleted when selecting predefined image: $deleted")
                                            }
                                        } catch (e: Exception) {
                                            println("[DEBUG_LOG] Error deleting custom image: ${e.message}")
                                        }
                                    }

                                    customImageUri = null  // Clear a custom image when selecting a predefined one
                                }
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = "Holiday Image Option",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Option to select an image from a gallery
                Text(
                    text = "Or select from your device:",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                        .align(Alignment.Start)
                )

                // Image picker button
                ImagePickerButton(
                    onImageSelected = { uri ->
                        println("[DEBUG_LOG] Image selected in EditHolidayDialog: $uri")

                        // Save the image to internal storage
                        val internalUri = imageStorageManager.saveImageToInternalStorage(uri)

                        if (internalUri != null) {
                            println("[DEBUG_LOG] Image saved to internal storage: $internalUri")

                            // Delete the old custom image if it exists and is an internal storage URI
                            if (!customImageUri.isNullOrEmpty()) {
                                try {
                                    val oldUri = Uri.parse(customImageUri)
                                    if (imageStorageManager.isInternalStorageUri(oldUri)) {
                                        val deleted = imageStorageManager.deleteImageFromInternalStorage(oldUri)
                                        println("[DEBUG_LOG] Old image deleted: $deleted")
                                    }
                                } catch (e: Exception) {
                                    println("[DEBUG_LOG] Error deleting old image: ${e.message}")
                                }
                            }

                            // Update the custom image URI
                            customImageUri = internalUri.toString()
                            println("[DEBUG_LOG] Updated customImageUri: $customImageUri")
                        } else {
                            println("[DEBUG_LOG] Failed to save image to internal storage")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { cleanupUnsavedImages() }) {
                        Text("Cancel")
                    }

                    ElevatedButton(
                        onClick = { validateAndSave() },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Purple40,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Save Changes"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Save",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }

    // Request focus on the text field when the dialog is shown
    androidx.compose.runtime.LaunchedEffect(Unit) {
        focusRequester.requestFocus()
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

                // Update the holiday date
                holidayDate = "$day $month"
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}
