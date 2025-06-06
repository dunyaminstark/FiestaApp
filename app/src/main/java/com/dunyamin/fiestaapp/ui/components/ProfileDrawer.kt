package com.dunyamin.fiestaapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dunyamin.fiestaapp.R
import com.dunyamin.fiestaapp.ui.theme.Purple40
import com.dunyamin.fiestaapp.ui.theme.PurpleGrey40
import com.dunyamin.fiestaapp.ui.theme.Pink40

@Composable
fun ProfileDrawerContent(
    onUpdateInfoClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onSignupClick: () -> Unit = {}
) {
    var showSettingsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(350.dp)
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image (circular)
        Image(
            painter = painterResource(id = R.drawable.profile_photo), // Replace it with the actual profile image
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Username
        Text(
            text = "John Doe",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // User Bio
        Text(
            text = "Android Developer | Holiday Enthusiast",
            fontSize = 15.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        Spacer(modifier = Modifier.height(32.dp))

        // Settings Option
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { showSettingsDialog = true },
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Settings",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    // Settings Dialog
    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { showSettingsDialog = false },
            onUpdateInfoClick = {
                showSettingsDialog = false
                onUpdateInfoClick()
            },
            onLoginClick = {
                showSettingsDialog = false
                onLoginClick()
            },
            onSignupClick = {
                showSettingsDialog = false
                onSignupClick()
            }
        )
    }
}

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onUpdateInfoClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier.width(320.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Header
                Text(
                    text = "Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Purple40,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )

                // Account Section
                Text(
                    text = "Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Update Info Button - Elevated Button with Icon
                ElevatedButton(
                    onClick = onUpdateInfoClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White,
                        contentColor = Purple40
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Update Profile",
                            tint = Purple40
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Update Profile",
                            fontSize = 16.sp
                        )
                    }
                }

                // Authentication Section
                Text(
                    text = "Authentication",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )

                // Login Button - Elevated Button with Icon
                ElevatedButton(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White,
                        contentColor = PurpleGrey40
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Login,
                            contentDescription = "Login",
                            tint = PurpleGrey40
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Login",
                            fontSize = 16.sp
                        )
                    }
                }

                // Signup Button - Elevated Button with Icon
                ElevatedButton(
                    onClick = onSignupClick,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White,
                        contentColor = Pink40
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Sign Up",
                            tint = Pink40
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Sign Up",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
