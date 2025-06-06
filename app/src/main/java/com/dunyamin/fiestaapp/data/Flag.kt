package com.dunyamin.fiestaapp.data

import androidx.annotation.DrawableRes

/**
 * Represents a country flag with its code, name, and image.
 * 
 * @property countryCode The ISO 3166-1 alpha-2 country code (e.g., "tr" for Turkey)
 * @property countryName The name of the country
 * @property flagResource The resource ID of the flag image (used for local resources)
 * @property flagUrl The URL of the flag image (used when loading from API)
 */
data class Flag(
    val countryCode: String,
    val countryName: String,
    @DrawableRes val flagResource: Int = 0,
    val flagUrl: String? = null
) {
    /**
     * Returns true if this flag has a URL for its image.
     */
    fun hasUrl(): Boolean {
        return !flagUrl.isNullOrEmpty()
    }
}
