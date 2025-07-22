package com.dunyamin.fiestaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // Import collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dunyamin.fiestaapp.ui.screens.LoginDialog
import com.dunyamin.fiestaapp.ui.screens.MainScreen
import com.dunyamin.fiestaapp.ui.screens.SignupDialog
import com.dunyamin.fiestaapp.ui.screens.UpdateInfoDialog
import com.dunyamin.fiestaapp.ui.screens.VotingScreen
import com.dunyamin.fiestaapp.viewmodel.ProfileViewModel

// Define navigation routes
object AppRoutes {
    const val MAIN = "main"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppRoutes.MAIN,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    // State variables to control dialog visibility
    var showLoginDialog by remember { mutableStateOf(false) }
    var showSignupDialog by remember { mutableStateOf(false) }
    var showUpdateInfoDialog by remember { mutableStateOf(false) }

    // Collect name and bio from ViewModel to pass to UpdateInfoDialog
    val currentName by profileViewModel.name.collectAsState()
    val currentBio by profileViewModel.bio.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppRoutes.MAIN) {
            MainScreen(
                onUpdateInfoClick = { showUpdateInfoDialog = true },
                onLoginClick = { showLoginDialog = true },
                onSignupClick = { showSignupDialog = true },
                onVoteClick = {
                    navController.navigate("voting_screen/20 April")
                }
            )

            // Show dialogs when needed
            if (showLoginDialog) {
                LoginDialog(
                    onDismiss = { showLoginDialog = false },
                    onLogin = { email, password ->
                        // TODO: Handle login logic
                        showLoginDialog = false
                    }
                )
            }

            if (showSignupDialog) {
                SignupDialog(
                    onDismiss = { showSignupDialog = false },
                    onSignup = { name, email, password ->
                        // TODO: Handle signup logic
                        showSignupDialog = false
                    }
                )
            }

            if (showUpdateInfoDialog) {
                UpdateInfoDialog(
                    initialName = currentName, // Pass the current name
                    initialBio = currentBio,   // Pass current bio
                    onDismiss = { showUpdateInfoDialog = false },
                    onSave = { name, bio ->
                        profileViewModel.saveProfile(name, bio) // Call ViewModel to save
                        showUpdateInfoDialog = false
                    }
                )
            }
        }

        composable("voting_screen/{date}") { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: "Unknown Date"
            VotingScreen(date = date)
        }


    }
}