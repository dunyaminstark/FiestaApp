package com.dunyamin.fiestaapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Fiesta app.
 * This is required for Hilt dependency injection.
 */
@HiltAndroidApp
class FiestaApplication : Application()