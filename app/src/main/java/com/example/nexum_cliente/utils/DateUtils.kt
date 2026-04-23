package com.example.nexum_cliente.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    const val DATE_PATTERN = "dd/MM/yyyy"
    const val TIME_PATTERN = "hh:mm a"

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatToISO8601(date: String, time: String): String {
        return try {
            Log.d("DateUtils", "formatToISO8601: $date $time")
            // Define the formatter for the input date format "dd/MM/yyyy"
            val dateInputFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.getDefault())
            val localDate = LocalDate.parse(date, dateInputFormatter)

            // Normalizar AM/PM: eliminar puntos y convertir a mayúsculas (ej: "a.m." -> "AM")
            val normalizedTime = time.replace(".", "").uppercase(Locale.ENGLISH).trim()

            // Define the formatter for the input time format "hh:mm a" (e.g., "12:00 AM")
            val timeInputFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN, Locale.ENGLISH)
            val localTime = LocalTime.parse(normalizedTime, timeInputFormatter)

            val localDateTime = LocalDateTime.of(localDate, localTime)

            val systemZoneId = ZoneId.systemDefault()
            val zonedDateTime = ZonedDateTime.of(localDateTime, systemZoneId)

            zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        } catch (e: Exception) {
            Log.e("DateUtils", "Error al formatear la fecha y hora: $e")
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatIsoToReadable(isoDate: String): String {
        return try {
            // Parsea la fecha ISO 8601 (ej: 2026-02-05T21:29:43.901965-05:00)
            val zonedDateTime = ZonedDateTime.parse(isoDate)

            // Formato de salida deseado: "dd/MM/yyyy hh:mm a"
            val outputFormatter =
                DateTimeFormatter.ofPattern("$DATE_PATTERN $TIME_PATTERN", Locale.getDefault())
            zonedDateTime.format(outputFormatter)
        } catch (e: Exception) {
            Log.e("DateUtils", "Error al parsear la fecha ISO: $e")
            LocalDate.now().toString()
        }
    }
}
