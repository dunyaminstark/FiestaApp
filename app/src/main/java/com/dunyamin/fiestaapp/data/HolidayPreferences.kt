package com.dunyamin.fiestaapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit

/**
 * Utility class to handle saving and loading holiday data using SharedPreferences.
 */
class HolidayPreferences(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "holiday_preferences"
        private const val KEY_PREFIX = "holiday_name_"
        private const val IMAGE_KEY_PREFIX = "holiday_image_"
        private const val CUSTOM_IMAGE_KEY_PREFIX = "holiday_custom_image_"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Save a holiday name for a specific date.
     * 
     * @param date The date of the holiday in string format
     * @param name The name of the holiday
     */
    fun saveHolidayName(date: String, name: String) {
        sharedPreferences.edit().putString("$KEY_PREFIX$date", name).apply()
    }

    /**
     * Get a holiday name for a specific date.
     * 
     * @param date The date of the holiday in string format
     * @param defaultName The default name to return if no saved name is found
     * @return The saved holiday name or the default name
     */
    fun getHolidayName(date: String, defaultName: String): String {
        return sharedPreferences.getString("$KEY_PREFIX$date", defaultName) ?: defaultName
    }

    /**
     * Check if a holiday name has been saved for a specific date.
     * 
     * @param date The date of the holiday in string format
     * @return True if a name has been saved, false otherwise
     */
    fun hasHolidayName(date: String): Boolean {
        return sharedPreferences.contains("$KEY_PREFIX$date")
    }

    /**
     * Get all saved holiday names as a map of date to name.
     * 
     * @return A map of date strings to holiday names
     */
    fun getAllSavedHolidayNames(): Map<String, String> {
        val result = mutableMapOf<String, String>()

        sharedPreferences.all.forEach { (key, value) ->
            if (key.startsWith(KEY_PREFIX) && value is String) {
                val date = key.removePrefix(KEY_PREFIX)
                result[date] = value
            }
        }

        return result
    }

    /**
     * Save an image resource ID for a specific date.
     * 
     * @param date The date of the holiday in string format
     * @param imageRes The resource ID of the image
     */
    fun saveHolidayImageRes(date: String, imageRes: Int) {
        sharedPreferences.edit { putInt("$IMAGE_KEY_PREFIX$date", imageRes) }
    }

    /**
     * Get an image resource ID for a specific date.
     * 
     * @param date The date of the holiday in string format
     * @param defaultImageRes The default resource ID to return if no saved resource ID is found
     * @return The saved resource ID or the default resource ID
     */
    fun getHolidayImageRes(date: String, defaultImageRes: Int): Int {
        return sharedPreferences.getInt("$IMAGE_KEY_PREFIX$date", defaultImageRes)
    }

    /**
     * Check if an image resource ID has been saved for a specific date.
     * 
     * @param date The date of the holiday in string format
     * @return True if a resource ID has been saved, false otherwise
     */
    fun hasHolidayImageRes(date: String): Boolean {
        return sharedPreferences.contains("$IMAGE_KEY_PREFIX$date")
    }

    /**
     * Save a custom image URI for a specific date.
     * 
     * @param date The date of the holiday in string format
     * @param imageUri The URI of the custom image
     */
    fun saveCustomHolidayImage(date: String, imageUri: String) {
        println("[DEBUG_LOG] HolidayPreferences: Saving custom image URI for date $date: $imageUri")
        val key = "$CUSTOM_IMAGE_KEY_PREFIX$date"
        sharedPreferences.edit { putString(key, imageUri) }

        // Verify the save operation
        val savedUri = sharedPreferences.getString(key, null)
        println("[DEBUG_LOG] HolidayPreferences: Verified saved URI: $savedUri")
    }

    /**
     * Remove a custom image URI for a specific date.
     * 
     * @param date The date of the holiday in string format
     */
    fun removeCustomHolidayImage(date: String) {
        println("[DEBUG_LOG] HolidayPreferences: Removing custom image URI for date $date")
        val key = "$CUSTOM_IMAGE_KEY_PREFIX$date"
        sharedPreferences.edit { remove(key) }

        // Verify the remove operation
        val removedUri = sharedPreferences.getString(key, null)
        println("[DEBUG_LOG] HolidayPreferences: Verified after removal, URI is null: ${removedUri == null}")
    }

    /**
     * Get a custom image URI for a specific date.
     * 
     * @param date The date of the holiday in string format
     * @return The saved URI or null if no custom image has been saved
     */
    fun getCustomHolidayImage(date: String): String? {
        val key = "$CUSTOM_IMAGE_KEY_PREFIX$date"
        val uri = sharedPreferences.getString(key, null)
        println("[DEBUG_LOG] HolidayPreferences: Getting custom image URI for date $date: $uri")
        return uri
    }

    /**
     * Check if a custom image has been saved for a specific date.
     * 
     * @param date The date of the holiday in string format
     * @return True if a custom image has been saved, false otherwise
     */
    fun hasCustomHolidayImage(date: String): Boolean {
        return sharedPreferences.contains("$CUSTOM_IMAGE_KEY_PREFIX$date")
    }
}

/**
 * Composable function to get the HolidayPreferences instance.
 */
@Composable
fun rememberHolidayPreferences(): HolidayPreferences {
    val context = LocalContext.current
    return HolidayPreferences(context)
}
