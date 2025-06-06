package com.dunyamin.fiestaapp.data.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.dunyamin.fiestaapp.data.rememberHolidayPreferences

/**
 * Provides a HolidayRepository instance for use in Composable functions.
 */
object HolidayRepositoryProvider {
    
    /**
     * Returns a remembered instance of HolidayRepository for use in Composable functions.
     * 
     * @return A SharedPreferencesHolidayRepository instance
     */
    @Composable
    fun rememberHolidayRepository(): HolidayRepository {
        val holidayPreferences = rememberHolidayPreferences()
        return remember(holidayPreferences) {
            SharedPreferencesHolidayRepository(holidayPreferences)
        }
    }
}