package com.dunyamin.fiestaapp.data

import androidx.annotation.DrawableRes

/**
 * Represents a holiday with a name, date, and associated images.
 * This class follows proper encapsulation principles and includes validation.
 *
 * @property name The name of the holiday
 * @property date The date of the holiday in string format
 * @property imageRes The resource ID of the default image for the holiday
 * @property customImageUri Optional URI of a custom image for the holiday
 */
data class Holiday(
    val name: String,
    val date: String,
    @DrawableRes val imageRes: Int,
    val customImageUri: String? = null
) {
    init {
        require(name.isNotBlank()) { "Holiday name cannot be blank" }
        require(date.isNotBlank()) { "Holiday date cannot be blank" }
        require(imageRes != 0) { "Image resource ID must not be 0" }
    }

    /**
     * Returns true if this holiday has a custom image.
     */
    fun hasCustomImage(): Boolean {
        return !customImageUri.isNullOrEmpty()
    }

    /**
     * Creates a copy of this holiday with an updated name.
     *
     * @param newName The new name for the holiday
     * @return A new Holiday instance with the updated name
     */
    fun withName(newName: String): Holiday {
        return copy(name = newName)
    }

    /**
     * Creates a copy of this holiday with an updated date.
     *
     * @param newDate The new date for the holiday
     * @return A new Holiday instance with the updated date
     */
    fun withDate(newDate: String): Holiday {
        return copy(date = newDate)
    }

    /**
     * Creates a copy of this holiday with an updated image resource.
     *
     * @param newImageRes The new image resource ID for the holiday
     * @return A new Holiday instance with the updated image resource
     */
    fun withImageRes(newImageRes: Int): Holiday {
        return copy(imageRes = newImageRes)
    }

    /**
     * Creates a copy of this holiday with an updated custom image URI.
     *
     * @param newCustomImageUri The new custom image URI for the holiday
     * @return A new Holiday instance with the updated custom image URI
     */
    fun withCustomImageUri(newCustomImageUri: String?): Holiday {
        return copy(customImageUri = newCustomImageUri)
    }

    companion object {
        /**
         * Creates a builder for constructing Holiday instances.
         *
         * @return A new HolidayBuilder instance
         */
        fun builder(): HolidayBuilder {
            return HolidayBuilder()
        }
    }

    /**
     * Builder class for creating Holiday instances.
     */
    class HolidayBuilder {
        private var name: String = ""
        private var date: String = ""
        private var imageRes: Int = 0
        private var customImageUri: String? = null

        /**
         * Sets the name for the holiday.
         *
         * @param name The name of the holiday
         * @return This builder for method chaining
         */
        fun name(name: String): HolidayBuilder {
            this.name = name
            return this
        }

        /**
         * Sets the date for the holiday.
         *
         * @param date The date of the holiday in string format
         * @return This builder for method chaining
         */
        fun date(date: String): HolidayBuilder {
            this.date = date
            return this
        }

        /**
         * Sets the image resource ID for the holiday.
         *
         * @param imageRes The resource ID of the default image for the holiday
         * @return This builder for method chaining
         */
        fun imageRes(imageRes: Int): HolidayBuilder {
            this.imageRes = imageRes
            return this
        }

        /**
         * Sets the custom image URI for the holiday.
         *
         * @param customImageUri The URI of a custom image for the holiday
         * @return This builder for method chaining
         */
        fun customImageUri(customImageUri: String?): HolidayBuilder {
            this.customImageUri = customImageUri
            return this
        }

        /**
         * Builds a new Holiday instance with the configured properties.
         *
         * @return A new Holiday instance
         * @throws IllegalArgumentException if any required properties are invalid
         */
        fun build(): Holiday {
            return Holiday(
                name = name,
                date = date,
                imageRes = imageRes,
                customImageUri = customImageUri
            )
        }
    }
}
