package com.example.nexum_cliente.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.nexum_cliente.ui.navigation.rutes.AuthRoutes

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 21/08/2025
 * @version 1.0
 */
@Composable
fun TosAndPolicyText(
    initTxt: String = "",
    signUpTxt: String = "",
    policyTxt: String = "",
    go: (Any) -> Unit,
    textStyle: TextStyle = TextStyle(
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    ),
    defaultTextStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Light
    ),
    defaultLinkStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.None,
        fontWeight = FontWeight.Normal
    ),
) {
    RichClickableText(
        modifier = Modifier
            .fillMaxWidth(),
        style = textStyle,
        defaultTextStyle = defaultTextStyle,
        defaultLinkStyle = defaultLinkStyle
    ) {
        text(initTxt)
        link(signUpTxt) { go(AuthRoutes.SignUpScreen) }
        text(" y ")
        link(policyTxt) { go(AuthRoutes.SignUpScreen) }
    }
}
