package com.example.nexum_cliente.utils

import java.util.Locale
import kotlin.math.roundToInt

/**
 * Utilidades para formatear distancias de forma legible (ej. 418 m, 2.5 km).
 *
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/28/2026
 * @version 1.0
 */
object DistanceUtils {

    fun formatDistance(rawDistance: String?): String {
        if (rawDistance.isNullOrBlank()) return ""
        val cleaned = rawDistance.trim()
        val meters = cleaned.toDoubleOrNull()
        if (meters != null) {
            return if (meters >= 1000.0) {
                String.format(Locale.US, "%.1f km", meters / 1000.0)
            } else {
                "${meters.roundToInt()} m"
            }
        }
        val regex = Regex("""([0-9]+(?:\.[0-9]+)?)\s*(km|m)?""", RegexOption.IGNORE_CASE)
        val match = regex.find(cleaned)
        if (match != null) {
            val num = match.groupValues[1].toDoubleOrNull()
            val unit = match.groupValues[2].lowercase()
            if (num != null) {
                return if (unit == "km") {
                    String.format(Locale.US, "%.1f km", num)
                } else if (unit == "m" && num >= 1000.0) {
                    String.format(Locale.US, "%.1f km", num / 1000.0)
                } else if (unit == "m") {
                    "${num.roundToInt()} m"
                } else if (num >= 1000.0) {
                    String.format(Locale.US, "%.1f km", num / 1000.0)
                } else {
                    "${num.roundToInt()} m"
                }
            }
        }
        return cleaned
    }

    fun formatDistance(distanceInMeters: Double?): String {
        if (distanceInMeters == null || distanceInMeters < 0) return ""
        return if (distanceInMeters >= 1000.0) {
            String.format(Locale.US, "%.1f km", distanceInMeters / 1000.0)
        } else {
            "${distanceInMeters.roundToInt()} m"
        }
    }
}
