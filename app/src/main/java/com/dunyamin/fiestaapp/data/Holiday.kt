package com.dunyamin.fiestaapp.data

import androidx.annotation.DrawableRes

data class Holiday(
    val name: String,
    val date: String,
    @DrawableRes val imageRes: Int,
    val customImageUri: String? = null
) {
    /**
     * Returns true if this holiday has a custom image.
     */
    fun hasCustomImage(): Boolean {
        return !customImageUri.isNullOrEmpty()
    }
}
