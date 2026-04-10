package com.example.nexum_cliente.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.toLowerCase
import com.example.nexum_cliente.utils.DateUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(): LocalDateTime {
    return try {
        when {
            this.contains("T") -> {
                // Intenta parsear con zona/offset primero, si falla intenta como local ISO
                try {
                    ZonedDateTime.parse(this).toLocalDateTime()
                } catch (e: Exception) {
                    LocalDateTime.parse(this)
                }
            }

            this.length == 10 -> { // yyyy-MM-dd
                val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                LocalDate.parse(this, pattern).atStartOfDay()
            }

            else -> {
                val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                LocalDateTime.parse(this, pattern)
            }
        }
    } catch (e: Exception) {
        LocalDateTime.now()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.formatIsoToReadableDate(): String {
    return try {
        this.toLocalDateTime().formatToReadableDate()
    } catch (e: Exception) {
        "NaN"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.formatIsoDate(): String {
    return try {
        DateUtils.formatIsoToReadable(this)
    } catch (e: Exception) {
        "NaN"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatToReadableDate(): String {
    val formatter =
        DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'a las' h:mm a", Locale("es", "ES"))
    val formattedDate = this.format(formatter)
    return formattedDate.split(" ").joinToString(" ") { word ->
        if (word.lowercase() !in listOf("de", "a", "las")) {
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } else {
            word
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatToDateOnly(): String {
    val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    val formattedDate = this.format(formatter)
    return formattedDate.split(" ").joinToString(" ") { word ->
        if (word.lowercase() !in listOf("de")) {
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } else {
            word
        }
    }
}

fun String.capitalizeFirstLetter(): String {
    return lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
