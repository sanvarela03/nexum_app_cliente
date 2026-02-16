package com.example.nexum_cliente.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatToISO8601(date: String, time: String): String {
        return try {
            val localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
            val localTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME)
            val localDateTime = LocalDateTime.of(localDate, localTime)

            val systemZoneId = ZoneId.systemDefault()
            val zonedDateTime = ZonedDateTime.of(localDateTime, systemZoneId)

            zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        } catch (e: Exception) {
            // Si el formato falla, devuelve una cadena vacía o maneja el error como prefieras.
            ""
        }
    }
}