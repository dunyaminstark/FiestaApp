package com.dunyamin.fiestaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.dunyamin.fiestaapp.navigation.AppNavigation
import com.dunyamin.fiestaapp.ui.theme.FiestaAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Tell the window that we're handling insets ourselves
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 2. Hide the system bars (status bar and navigation bar)
        hideSystemBars()

        // enableEdgeToEdge() is good, but for full control, the above is more explicit
        // for hiding. If you still want the edge-to-edge behavior when system bars
        // are temporarily shown, this is fine. Otherwise, you might not need it
        // if you're always hiding the bars. For now, I'll leave it commented out
        // as hideSystemBars() will take precedence for hiding.
        // enableEdgeToEdge()

        setContent {
            FiestaAppTheme {
                AppNavigation()
            }
        }
    }

    private fun hideSystemBars() {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}
