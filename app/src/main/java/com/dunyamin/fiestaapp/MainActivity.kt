package com.dunyamin.fiestaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dunyamin.fiestaapp.navigation.AppNavigation
import com.dunyamin.fiestaapp.ui.theme.FiestaAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FiestaAppTheme {
                AppNavigation()
            }
        }
    }
}
