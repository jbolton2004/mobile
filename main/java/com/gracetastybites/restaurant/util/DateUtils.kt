package com.gracetastybites.restaurant.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val DATE_FORMAT = "yyyy-MM-dd"
    private const val TIME_FORMAT = "HH:mm"
    private const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm"
    private const val MONTH_YEAR_FORMAT = "MMMM yyyy"

    private val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    private val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
    private val dateTimeFormatter = SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault())
    private val monthYearFormatter = SimpleDateFormat(MONTH_YEAR_FORMAT, Locale.getDefault())

    fun formatDate(date: Date): String {
        return dateFormatter.format(date)
    }

    fun formatTime(date: Date): String {
        return timeFormatter.format(date)
    }

    fun formatDateTime(date: Date): String {
        return dateTimeFormatter.format(date)
    }

    fun formatMonthYear(date: Date): String {
        return monthYearFormatter.format(date)
    }

    fun parseDate(dateString: String): Date? {
        return try {
            dateFormatter.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun getMonthStartDate(year: Int, month: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun getMonthEndDate(year: Int, month: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 23, 59, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return calendar.time
    }

    fun getCurrentMonthYear(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        return Pair(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
    }
}