package com.dunyamin.fiestaapp.data.repository

import com.dunyamin.fiestaapp.data.Holiday
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Repository interface for managing Holiday data.
 * This interface abstracts the data operations for holidays, providing a clean separation
 * between the data layer and the UI layer.
 */
interface HolidayRepository {
    /**
     * Get a holiday for a specific date.
     *
     * @param date The date for which to get the holiday
     * @param defaultName The default name to use if no holiday is found
     * @param defaultImageRes The default image resource to use if no holiday is found
     * @return The Holiday object for the specified date
     */
    fun getHoliday(date: LocalDate, defaultName: String, defaultImageRes: Int): Holiday

    /**
     * Get all holidays for a year.
     *
     * @param year The year for which to get holidays
     * @param defaultNames List of default names to use for holidays
     * @param defaultImageResIds List of default image resources to use for holidays
     * @return List of Holiday objects for the specified year
     */
    fun getHolidaysForYear(
        year: Int,
        defaultNames: List<String>,
        defaultImageResIds: List<Int>
    ): List<Holiday>

    /**
     * Save a holiday.
     *
     * @param holiday The Holiday object to save
     */
    fun saveHoliday(holiday: Holiday)

    /**
     * Check if a holiday exists for a specific date.
     *
     * @param date The date to check
     * @return True if a holiday exists for the date, false otherwise
     */
    fun hasHoliday(date: LocalDate): Boolean

    /**
     * Get the date formatter used by the repository.
     *
     * @return The DateTimeFormatter used for formatting dates
     */
    fun getDateFormatter(): DateTimeFormatter
}