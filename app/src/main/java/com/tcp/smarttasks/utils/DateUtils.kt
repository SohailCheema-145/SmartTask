package com.tcp.smarttasks.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    private const val INPUT_DATE_FORMAT = "yyyy-MM-dd"
    private const val DATE_DISPLAY_FORMAT = "MMM dd yyyy"

    fun getFormattedTodayDate(): String {
        // Create a SimpleDateFormat instance
        val dateFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
        // Get today's date
        val today = Calendar.getInstance().time
        // Format the date
        return dateFormat.format(today)
    }

    fun getDateDisplayFormat(input: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
        val outputFormat = SimpleDateFormat(DATE_DISPLAY_FORMAT, Locale.getDefault())

        // Parse the input date
        val date = inputFormat.parse(input)

        // Format the date to the desired output format
        return outputFormat.format(date)
    }

    fun getDaysLeftUntilDueDate(date: String?): String {
        if (date == null)
            return "n/a"
        // Define the date format
        val dateFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())

        // Parse the due date
        val dueDate = dateFormat.parse(date) ?: return "n/a"

        // Get today's date
        val today = Calendar.getInstance().time

        // Calculate the difference in milliseconds
        val differenceInMillis = dueDate.time - today.time

        // Convert milliseconds to days
        return (differenceInMillis / (1000 * 60 * 60 * 24)).toInt().toString()
    }

    fun getPreviousDay(dateString: String): String? {
        // Define the date format
        val dateFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())

        // Parse the input date string
        val date = dateFormat.parse(dateString) ?: return null

        // Create a Calendar instance and set it to the parsed date
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Get previous day
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return dateFormat.format(calendar.time)
    }

    fun getNextDay(dateString: String): String? {
        // Define the date format
        val dateFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())

        // Parse the input date string
        val date = dateFormat.parse(dateString) ?: return null

        // Create a Calendar instance and set it to the parsed date
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Get next day
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return dateFormat.format(calendar.time)
    }

    fun isToday(dateString: String): Boolean {
        // Define the date format
        val dateFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())

        // Parse the input date string
        val inputDate = dateFormat.parse(dateString) ?: return false

        // Get today's date
        val today = Calendar.getInstance()
        today.time = Calendar.getInstance().time

        // Set hours, minutes, seconds, and milliseconds to 0 for comparison
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        // Create a Calendar instance for the input date
        val inputCalendar = Calendar.getInstance()
        inputCalendar.time = inputDate
        inputCalendar.set(Calendar.HOUR_OF_DAY, 0)
        inputCalendar.set(Calendar.MINUTE, 0)
        inputCalendar.set(Calendar.SECOND, 0)
        inputCalendar.set(Calendar.MILLISECOND, 0)

        // Compare the two dates
        return today.timeInMillis == inputCalendar.timeInMillis
    }
}