package com.example.nexum_cliente.utils

import android.os.Build
<<<<<<< Updated upstream
=======
import android.util.Log
>>>>>>> Stashed changes
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
<<<<<<< Updated upstream
=======
import java.util.Locale
>>>>>>> Stashed changes

object DateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatToISO8601(date: String, time: String): String {
        return try {
<<<<<<< Updated upstream
            val localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
            val localTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME)
=======
            Log.d("DateUtils", "formatToISO8601: $date $time")
            // Define the formatter for the input date format "dd/MM/yyyy"
            val dateInputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
            val localDate = LocalDate.parse(date, dateInputFormatter)

            // Define the formatter for the input time format "hh:mm a" (e.g., "03:00 PM")
            val timeInputFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            val localTime = LocalTime.parse(time, timeInputFormatter)

>>>>>>> Stashed changes
            val localDateTime = LocalDateTime.of(localDate, localTime)

            val systemZoneId = ZoneId.systemDefault()
            val zonedDateTime = ZonedDateTime.of(localDateTime, systemZoneId)

            zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        } catch (e: Exception) {
            // Si el formato falla, devuelve una cadena vacía o maneja el error como prefieras.
<<<<<<< Updated upstream
=======
            Log.e("DateUtils", "Error al formatear la fecha y hora: $e")
>>>>>>> Stashed changes
            ""
        }
    }
}