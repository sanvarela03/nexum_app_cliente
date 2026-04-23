package com.example.nexum_cliente.utils.validator

import com.example.nexum_cliente.ui.common.ValidationResult
import com.example.nexum_cliente.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object JobOfferValidator {
    // Margen configurable en minutos para permitir el envío de la oferta
    var bufferMinutes: Int = 0

    fun validateTime(
        requestedDate: String,
        requestedTime: String,
        buffer: Int = bufferMinutes
    ): ValidationResult {
        if (requestedDate.isBlank() || requestedTime.isBlank()) {
            return ValidationResult(false)
        }

        return try {
            val timeFormatter = SimpleDateFormat(DateUtils.TIME_PATTERN, Locale.ENGLISH)
            val date = timeFormatter.parse(requestedTime)
            val cal = Calendar.getInstance().apply {
                if (date != null) time = date
            }
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            // 1. Rango laboral: 7 AM a 10 PM
            val inRange = hour >= 7 && (hour < 22 || (hour == 22 && minute == 0))
            if (!inRange) {
                return ValidationResult(false, "El horario de atención es de 7:00 AM a 10:00 PM.")
            }

            // 2. Si es hoy, validar que sea futuro considerando el buffer
            val sdf = SimpleDateFormat(DateUtils.DATE_PATTERN, Locale.getDefault())
            val todayStr = sdf.format(Date())

            if (requestedDate == todayStr) {
                val now = Calendar.getInstance()
                val currentH = now.get(Calendar.HOUR_OF_DAY)
                val currentM = now.get(Calendar.MINUTE)

                // Caso: Hora en el pasado literal
                if (hour < currentH || (hour == currentH && minute < currentM)) {
                    return ValidationResult(false, "La hora seleccionada ya pasó.")
                }

                // Caso: Margen de tiempo insuficiente (buffer)
                val calendarLimit = Calendar.getInstance().apply {
                    add(Calendar.MINUTE, buffer)
                }

                val limitH = calendarLimit.get(Calendar.HOUR_OF_DAY)
                val limitM = calendarLimit.get(Calendar.MINUTE)

                if (hour < limitH || (hour == limitH && minute < limitM)) {
                    return ValidationResult(false, "Debes seleccionar una hora con al menos $buffer minutos de anticipación.")
                }
            }
            ValidationResult(true)
        } catch (e: Exception) {
            ValidationResult(false, "Formato de hora inválido")
        }
    }

    // Sobrecarga para cuando ya se tienen los valores de hora/minuto (Picker)
    fun validateTime(
        hour: Int,
        minute: Int,
        requestedDate: String,
        buffer: Int = bufferMinutes
    ): ValidationResult {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val timeFormatter = SimpleDateFormat(DateUtils.TIME_PATTERN, Locale.ENGLISH)
        return validateTime(requestedDate, timeFormatter.format(calendar.time), buffer)
    }

    fun validateTitle(title: String): ValidationResult {
        return when {
            title.isBlank() -> ValidationResult(false, "El título no puede estar vacío.")
            title.length < 5 -> ValidationResult(false, "El título debe tener al menos 5 caracteres.")
            else -> ValidationResult(true)
        }
    }

    fun validateDescription(description: String): ValidationResult {
        return when {
            description.isBlank() -> ValidationResult(false, "La descripción no puede estar vacía.")
            description.length < 10 -> ValidationResult(false, "La descripción debe tener al menos 10 caracteres.")
            else -> ValidationResult(true)
        }
    }

    fun validateAddress(address: String): ValidationResult {
        return when {
            address.isBlank() -> ValidationResult(false, "La dirección no puede estar vacía.")
            address.length < 10 -> ValidationResult(false, "La dirección parece estar incompleta.")
            else -> ValidationResult(true)
        }
    }

    fun validateImages(images: List<Any>): ValidationResult {
        return if (images.isEmpty()) {
            ValidationResult(false, "Debes subir al menos una imagen.")
        } else {
            ValidationResult(true)
        }
    }

    fun validateDate(date: String): ValidationResult {
        return if (date.isBlank()) {
            ValidationResult(false, "Debes seleccionar una fecha.")
        } else {
            ValidationResult(true)
        }
    }

    fun isDateSelectable(utcTimeMillis: Long): Boolean {
        // Inicio del día de hoy en UTC
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val today = calendar.timeInMillis

        // Límite: actual + 2 años (hasta el final de ese año)
        val maxCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            add(Calendar.YEAR, 2)
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 31)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
        }
        val maxDate = maxCalendar.timeInMillis

        return utcTimeMillis >= today && utcTimeMillis <= maxDate
    }

    fun isYearSelectable(year: Int): Boolean {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return year >= currentYear && year <= currentYear + 2
    }
}
