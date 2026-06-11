package com.example.nexum_cliente.ui.components.creditcard

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Formats up to 16 raw digits as XXXX XXXX XXXX XXXX.
 * Spaces are inserted after every 4th digit.
 */
class CardNumberVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val formatted = buildString {
            raw.forEachIndexed { index, char ->
                if (index > 0 && index % 4 == 0) append(' ')
                append(char)
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Number of spaces inserted before this offset
                val spaces = when {
                    offset <= 4 -> 0
                    offset <= 8 -> 1
                    offset <= 12 -> 2
                    else -> 3
                }
                return (offset + spaces).coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Remove space characters that precede this transformed offset
                val charsUpToOffset = formatted.take(offset)
                return charsUpToOffset.count { it != ' ' }.coerceAtMost(raw.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

/**
 * Formats up to 4 raw digits as MM/YY.
 * The slash is inserted automatically after the 2nd digit.
 */
class ExpirationDateVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val formatted = buildString {
            raw.forEachIndexed { index, char ->
                if (index == 2) append('/')
                append(char)
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return if (offset <= 2) offset else (offset + 1).coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset == 3 -> 2 // cursor is on the slash — snap to before it
                    else -> (offset - 1).coerceAtMost(raw.length)
                }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

/**
 * Masks all CVV characters with a bullet (•).
 */
class CvvVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val masked = AnnotatedString("•".repeat(text.length))
        return TransformedText(masked, OffsetMapping.Identity)
    }
}
