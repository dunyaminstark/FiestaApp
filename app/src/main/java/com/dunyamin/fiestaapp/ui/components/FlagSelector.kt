package com.dunyamin.fiestaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dunyamin.fiestaapp.R // Assuming you have a default flag drawable
import com.dunyamin.fiestaapp.data.Flag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

// Function to fetch country codes from the API
suspend fun fetchCountryCodes(): Map<String, String> {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL("https://flagcdn.com/en/codes.json")
            val connection = url.openConnection()
            connection.connect()
            val result = connection.inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(result)
            val codes = mutableMapOf<String, String>()
            jsonObject.keys().forEach { key ->
                codes[key] = jsonObject.getString(key)
            }
            codes
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap() // Return an empty map in case of error
        }
    }
}

@Composable
fun FlagSelector(
    modifier: Modifier = Modifier,
    initialFlag: Flag,
    onFlagSelected: (Flag) -> Unit
) {
    var flags by remember { mutableStateOf<List<Flag>>(emptyList()) }
    var selectedFlag by remember { mutableStateOf(initialFlag) }
    var showDialog by remember { mutableStateOf(false) }

    // Fetch flags when the composable is first launched
    LaunchedEffect(Unit) {
        val countryCodes = fetchCountryCodes()
        flags = countryCodes.map { (code, name) ->
            Flag(code, name, "https://flagcdn.com/w320/$code.png")
        }
        // Update selectedFlag if initialFlag's code is in the new list or set a default
        val currentInitialFlag = flags.find { it.countryCode == initialFlag.countryCode }
        if (currentInitialFlag != null) {
            selectedFlag = currentInitialFlag
        } else if (flags.isNotEmpty()) {
            selectedFlag = flags.first() // Select the first flag as a default
            onFlagSelected(selectedFlag) // Notify about the new default selection
        }
    }

    // Flag preview that opens the dialog when clicked
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(selectedFlag.flagUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(id = R.drawable.logo), // Your placeholder
        error = painterResource(id = R.drawable.logo), // Your error placeholder
        contentDescription = "${selectedFlag.countryName} flag",
        modifier = modifier
            .size(40.dp)
            .clickable {
                if (flags.isNotEmpty()) { // Only show a dialog if flags are loaded
                    showDialog = true
                }
            }
    )

    // Dialog with flag options
    if (showDialog && flags.isNotEmpty()) {
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
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Color.Black
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(flag.flagUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.logo), // Your placeholder
            error = painterResource(id = R.drawable.logo), // Your error placeholder
            contentDescription = "${flag.countryName} flag",
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = flag.countryName,
            color = Color.Black
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}
