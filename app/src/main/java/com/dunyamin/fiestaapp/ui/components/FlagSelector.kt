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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dunyamin.fiestaapp.R
import com.dunyamin.fiestaapp.data.Flag
import com.dunyamin.fiestaapp.data.repository.CountryRepositoryProvider
import com.dunyamin.fiestaapp.viewmodel.CountryViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FlagSelector(
    modifier: Modifier = Modifier,
    initialFlag: Flag,
    onFlagSelected: (Flag) -> Unit
) {
    // Get the CountryViewModel
    val viewModel = hiltViewModel<CountryViewModel>()

    // Collect state from the ViewModel
    val countries by viewModel.countries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Trigger loading countries when the component is first composed
    LaunchedEffect(Unit) {
        viewModel.loadCountries()
    }

    // Use the countries from the API, or fall back to default flags if empty
    val flags = if (countries.isNotEmpty()) countries else viewModel.countryRepository.getDefaultFlags()

    var selectedFlag by remember { mutableStateOf(initialFlag) }
    var showDialog by remember { mutableStateOf(false) }

    // Flag preview that opens the dialog when clicked
    if (selectedFlag.hasUrl()) {
        // If the flag has a URL, use AsyncImage to load it
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(selectedFlag.flagUrl)
                .crossfade(true)
                .build(),
            contentDescription = "${selectedFlag.countryName} flag",
            modifier = modifier
                .size(40.dp)
                .clickable { showDialog = true }
        )
    } else {
        // Otherwise, use the local resource
        Image(
            painter = painterResource(id = selectedFlag.flagResource),
            contentDescription = "${selectedFlag.countryName} flag",
            modifier = modifier
                .size(40.dp)
                .clickable { showDialog = true }
        )
    }

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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Country",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (isLoading) {
                        // Show loading indicator
                        CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Loading countries...",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    } else if (error != null) {
                        // Show error message
                        Text(
                            text = "Error: $error",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Using default flags",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

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
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (flag.hasUrl()) {
            // If the flag has a URL, use AsyncImage to load it
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(flag.flagUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "${flag.countryName} flag",
                modifier = Modifier.size(32.dp)
            )
        } else {
            // Otherwise, use the local resource
            Image(
                painter = painterResource(id = flag.flagResource),
                contentDescription = "${flag.countryName} flag",
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = flag.countryName)
    }

    Spacer(modifier = Modifier.height(8.dp))
}
