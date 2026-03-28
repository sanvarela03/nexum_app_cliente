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
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatToISO8601(date: String, time: String): String {
        return try {
            Log.d("DateUtils", "formatToISO8601: $date $time")
            // Define the formatter for the input date format "dd/MM/yyyy"
            val dateInputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
            val localDate = LocalDate.parse(date, dateInputFormatter)

            // Normalizar AM/PM: eliminar puntos y convertir a mayúsculas (ej: "a.m." -> "AM")
            val normalizedTime = time.replace(".", "").uppercase(Locale.ENGLISH).trim()

            // Define the formatter for the input time format "hh:mm a" (e.g., "12:00 AM")
            val timeInputFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
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
}
