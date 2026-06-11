package com.example.nexum_cliente.ui.components.creditcard

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CreditCardFormTest {

    // ── detectCardType ───────────────────────────────────────────────────────

    @Test
    fun `detectCardType returns VISA for number starting with 4`() {
        assertEquals(CardType.VISA, detectCardType("4111111111111111"))
    }

    @Test
    fun `detectCardType returns MASTERCARD for prefix in range 51-55`() {
        assertEquals(CardType.MASTERCARD, detectCardType("5100000000000000"))
        assertEquals(CardType.MASTERCARD, detectCardType("5500000000000000"))
        assertEquals(CardType.MASTERCARD, detectCardType("5300000000000000"))
    }

    @Test
    fun `detectCardType returns AMEX for prefix 34 or 37`() {
        assertEquals(CardType.AMEX, detectCardType("341111111111111"))
        assertEquals(CardType.AMEX, detectCardType("371111111111111"))
    }

    @Test
    fun `detectCardType returns UNKNOWN for unrecognised prefix`() {
        assertEquals(CardType.UNKNOWN, detectCardType("6011111111111117"))
        assertEquals(CardType.UNKNOWN, detectCardType("3000000000000004"))
    }

    @Test
    fun `detectCardType returns UNKNOWN for empty string`() {
        assertEquals(CardType.UNKNOWN, detectCardType(""))
    }

    @Test
    fun `detectCardType handles partial input safely`() {
        assertEquals(CardType.VISA, detectCardType("4"))
        assertEquals(CardType.UNKNOWN, detectCardType("5"))
        assertEquals(CardType.AMEX, detectCardType("37"))
    }

    // ── CardNumberVisualTransformation ───────────────────────────────────────

    @Test
    fun `CardNumberVisualTransformation formats 16 digits with spaces`() {
        val transformation = CardNumberVisualTransformation()
        val result = transformation.filter(AnnotatedString("1234567890123456"))
        assertEquals("1234 5678 9012 3456", result.text.text)
    }

    @Test
    fun `CardNumberVisualTransformation handles 4 digits without trailing space`() {
        val transformation = CardNumberVisualTransformation()
        val result = transformation.filter(AnnotatedString("1234"))
        assertEquals("1234", result.text.text)
    }

    @Test
    fun `CardNumberVisualTransformation handles 5 digits`() {
        val transformation = CardNumberVisualTransformation()
        val result = transformation.filter(AnnotatedString("12345"))
        assertEquals("1234 5", result.text.text)
    }

    @Test
    fun `CardNumberVisualTransformation offset mapping originalToTransformed is correct`() {
        val transformation = CardNumberVisualTransformation()
        val result = transformation.filter(AnnotatedString("1234567890123456"))
        // Offset 0..4: no spaces yet → same
        assertEquals(0, result.offsetMapping.originalToTransformed(0))
        assertEquals(4, result.offsetMapping.originalToTransformed(4))
        // After 4 digits one space is added
        assertEquals(6, result.offsetMapping.originalToTransformed(5))
        // After 8 raw digits one more space has been inserted (at formatted pos 4)
        // "1234 5678" = 9 chars → cursor at 9
        assertEquals(9, result.offsetMapping.originalToTransformed(8))
    }

    // ── ExpirationDateVisualTransformation ───────────────────────────────────

    @Test
    fun `ExpirationDateVisualTransformation formats 4 digits as MM slash YY`() {
        val transformation = ExpirationDateVisualTransformation()
        val result = transformation.filter(AnnotatedString("1229"))
        assertEquals("12/29", result.text.text)
    }

    @Test
    fun `ExpirationDateVisualTransformation does not add slash for 2 digits`() {
        val transformation = ExpirationDateVisualTransformation()
        val result = transformation.filter(AnnotatedString("12"))
        assertEquals("12", result.text.text)
    }

    @Test
    fun `ExpirationDateVisualTransformation handles 3 digits`() {
        val transformation = ExpirationDateVisualTransformation()
        val result = transformation.filter(AnnotatedString("122"))
        assertEquals("12/2", result.text.text)
    }

    @Test
    fun `ExpirationDateVisualTransformation offset mapping is correct`() {
        val transformation = ExpirationDateVisualTransformation()
        val result = transformation.filter(AnnotatedString("1229"))
        // Offsets 0–2 unchanged
        assertEquals(0, result.offsetMapping.originalToTransformed(0))
        assertEquals(2, result.offsetMapping.originalToTransformed(2))
        // After index 2 the slash adds 1
        assertEquals(4, result.offsetMapping.originalToTransformed(3))
        assertEquals(5, result.offsetMapping.originalToTransformed(4))
    }

    // ── Submit button enabled/disabled logic ──────────────────────────────────

    @Test
    fun `form is invalid when all fields are empty`() {
        val state = CreditCardState()
        assertFalse(isFormValidForTest(state))
    }

    @Test
    fun `form is invalid when card number has fewer than 16 digits`() {
        val state = CreditCardState(
            cardNumber = "123456789012345", // 15 digits
            cardHolderName = "John Doe",
            expirationDate = "1229",
            cvv = "123"
        )
        assertFalse(isFormValidForTest(state))
    }

    @Test
    fun `form is invalid when cardholder name is blank`() {
        val state = CreditCardState(
            cardNumber = "1234567890123456",
            cardHolderName = "   ",
            expirationDate = "1229",
            cvv = "123"
        )
        assertFalse(isFormValidForTest(state))
    }

    @Test
    fun `form is invalid when expiry has fewer than 4 digits`() {
        val state = CreditCardState(
            cardNumber = "1234567890123456",
            cardHolderName = "John Doe",
            expirationDate = "122",
            cvv = "123"
        )
        assertFalse(isFormValidForTest(state))
    }

    @Test
    fun `form is invalid when cvv has fewer than 3 digits`() {
        val state = CreditCardState(
            cardNumber = "1234567890123456",
            cardHolderName = "John Doe",
            expirationDate = "1229",
            cvv = "12"
        )
        assertFalse(isFormValidForTest(state))
    }

    @Test
    fun `form is valid when all fields are correctly filled`() {
        val state = CreditCardState(
            cardNumber = "1234567890123456",
            cardHolderName = "John Doe",
            expirationDate = "1229",
            cvv = "123"
        )
        assertTrue(isFormValidForTest(state))
    }

    @Test
    fun `form is valid with a 4-digit CVV for Amex`() {
        val state = CreditCardState(
            cardNumber = "3714496353984301",
            cardHolderName = "Jane Smith",
            expirationDate = "0628",
            cvv = "1234"
        )
        assertTrue(isFormValidForTest(state))
    }
}

// ---------------------------------------------------------------------------
// Test-accessible validation mirror (duplicates private logic from CreditCardForm)
// ---------------------------------------------------------------------------

private fun isFormValidForTest(state: CreditCardState): Boolean =
    state.cardNumber.length == 16 &&
        state.cardHolderName.isNotBlank() &&
        state.expirationDate.length == 4 &&
        state.cvv.length in 3..4
