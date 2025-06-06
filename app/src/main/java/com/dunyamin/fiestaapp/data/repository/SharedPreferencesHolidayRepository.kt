package com.dunyamin.fiestaapp.data.repository

import com.dunyamin.fiestaapp.data.Holiday
import com.dunyamin.fiestaapp.data.HolidayPreferences
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter

/**
 * Implementation of HolidayRepository that uses SharedPreferences for storage.
 *
 * @param holidayPreferences The HolidayPreferences instance to use for storage
 */
class SharedPreferencesHolidayRepository(
    private val holidayPreferences: HolidayPreferences
) : HolidayRepository {

    private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM")

    /**
     * Get a holiday for a specific date.
     *
     * @param date The date for which to get the holiday
     * @param defaultName The default name to use if no holiday is found
     * @param defaultImageRes The default image resource to use if no holiday is found
     * @return The Holiday object for the specified date
     */
    override fun getHoliday(date: LocalDate, defaultName: String, defaultImageRes: Int): Holiday {
        val formattedDate = date.format(dateFormatter)
        val name = holidayPreferences.getHolidayName(formattedDate, defaultName)
        val imageRes = holidayPreferences.getHolidayImageRes(formattedDate, defaultImageRes)
        val customImageUri = holidayPreferences.getCustomHolidayImage(formattedDate)

        return Holiday(
            name = name,
            date = formattedDate,
            imageRes = imageRes,
            customImageUri = customImageUri
        )
    }

    /**
     * Get all holidays for a year.
     *
     * @param year The year for which to get holidays
     * @param defaultNames List of default names to use for holidays
     * @param defaultImageResIds List of default image resources to use for holidays
     * @return List of Holiday objects for the specified year
     */
    override fun getHolidaysForYear(
        year: Int,
        defaultNames: List<String>,
        defaultImageResIds: List<Int>
    ): List<Holiday> {
        val startDate = LocalDate.of(year, 1, 1)
        val isLeapYear = Year.isLeap(year.toLong())
        val daysInYear = if (isLeapYear) 366 else 365

        return List(daysInYear) { dayOffset ->
            val date = startDate.plusDays(dayOffset.toLong())
            val nameIndex = dayOffset % defaultNames.size
            val imageIndex = dayOffset % defaultImageResIds.size
            
            getHoliday(
                date = date,
                defaultName = defaultNames[nameIndex],
                defaultImageRes = defaultImageResIds[imageIndex]
            )
        }
    }

    /**
     * Save a holiday.
     *
     * @param holiday The Holiday object to save
     */
    override fun saveHoliday(holiday: Holiday) {
        holidayPreferences.saveHolidayName(holiday.date, holiday.name)
        holidayPreferences.saveHolidayImageRes(holiday.date, holiday.imageRes)
        
        if (holiday.hasCustomImage()) {
            holiday.customImageUri?.let { uri ->
                holidayPreferences.saveCustomHolidayImage(holiday.date, uri)
            }
        }
    }

    /**
     * Check if a holiday exists for a specific date.
     *
     * @param date The date to check
     * @return True if a holiday exists for the date, false otherwise
     */
    override fun hasHoliday(date: LocalDate): Boolean {
        val formattedDate = date.format(dateFormatter)
        return holidayPreferences.hasHolidayName(formattedDate)
    }

    /**
     * Get the date formatter used by the repository.
     *
     * @return The DateTimeFormatter used for formatting dates
     */
    override fun getDateFormatter(): DateTimeFormatter {
        return dateFormatter
    }
}