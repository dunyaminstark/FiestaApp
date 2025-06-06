package com.dunyamin.fiestaapp.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dunyamin.fiestaapp.R
import com.dunyamin.fiestaapp.ui.theme.Purple40

/**
 * A composable function that displays a holiday image, either from a resource ID or a custom URI.
 *
 * @param imageRes The resource ID of the image to display
 * @param customImageUri The URI of a custom image to display, if available
 * @param contentDescription The content description for the image
 * @param modifier The modifier to apply to the image
 */
@Composable
fun HolidayImage(
    imageRes: Int,
    customImageUri: String? = null,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    if (!customImageUri.isNullOrEmpty()) {
        // Display custom image from URI
        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(Uri.parse(customImageUri))
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            error = painterResource(id = R.drawable.card_image) // Fallback image if loading fails
        )
    } else {
        // Display image from resource ID
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}

/**
 * A composable function that provides an image picker button.
 * When clicked, it launches the system's image picker and returns the selected image URI.
 *
 * @param onImageSelected Callback function that is called when an image is selected
 */
@Composable
fun ImagePickerButton(
    onImageSelected: (Uri) -> Unit
) {
    // Create a launcher for the image picker activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // If an image was selected (uri is not null), call the callback function
        uri?.let { onImageSelected(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { launcher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple40,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.AddPhotoAlternate,
                contentDescription = "Add Photo",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Select from Gallery")
        }
    }
}
