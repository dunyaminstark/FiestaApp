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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dunyamin.fiestaapp.R
import com.dunyamin.fiestaapp.data.Holiday
import com.dunyamin.fiestaapp.ui.theme.Purple40
import com.dunyamin.fiestaapp.ui.components.ImagePickerButton
import com.dunyamin.fiestaapp.ui.components.HolidayImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHolidayDialog(
    holiday: Holiday,
    onDismiss: () -> Unit,
    onSave: (Holiday) -> Unit
) {
    var holidayName by remember { mutableStateOf(holiday.name) }
    var isError by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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

    // Validate input and handle save action
    fun validateAndSave() {
        if (holidayName.trim().isEmpty()) {
            isError = true
        } else {
            // Create a new Holiday with the updated name and image
            val updatedHoliday = Holiday(
                name = holidayName.trim(),
                date = holiday.date,
                imageRes = selectedImageRes,
                customImageUri = customImageUri
            )
            onSave(updatedHoliday)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
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
                                    customImageUri = null  // Clear custom image when selecting a predefined one
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

                // Option to select image from gallery
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
                        customImageUri = uri.toString()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
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
}
