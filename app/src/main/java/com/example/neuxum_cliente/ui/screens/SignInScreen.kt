package com.example.neuxum_cliente.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neuxum_cliente.R
import com.example.neuxum_cliente.ui.componets.DividerTextComponent
import com.example.neuxum_cliente.ui.componets.PasswordTextFieldComponent
import com.example.neuxum_cliente.ui.navigation.rutes.Routes
import com.example.protapptest.ui.components.ButtonComponent
import com.example.protapptest.ui.components.MyTextFieldComponent

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignInScreen(
    modifier: Modifier = Modifier,
    go: (Any) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )
        {
            Image(
                painter = painterResource(R.drawable.nexum_logo_2),
                modifier = Modifier
                    .size(140.dp),
                contentDescription = "Logo"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Ingresar a tu cuenta",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
            Text("Ingrese su correo para iniciar sesión", fontSize = 18.sp)

            MyTextFieldComponent(
                labelValue = "Ingrese su correo",
                icon = Icons.Default.Email,
                onTextSelected = {},
                errorStatus = true
            )
            PasswordTextFieldComponent(
                labelValue = "Ingrese su contraseña",
                icon = Icons.Default.Lock,
                onTextSelected = {},
                errorStatus = true
            )

            ClickableText(
                text = buildAnnotatedString { append("¿Olvidaste tu contraseña?") },
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Right,
                    textDecoration = TextDecoration.Underline,
                    color = Color(0xFF828282)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                onClick = {

                }
            )
            ButtonComponent(
                value = "Ingresar",
                isEnabled = true,
            ) {

            }
            DividerTextComponent()


            val initTxt = "¿No tienes una cuenta?  "
            val signUpTxt = "Registrarse"

            val annotatedString = buildAnnotatedString {
                append(initTxt)
                withStyle(style = SpanStyle(color = Color(0xFF828282))) {
                    pushStringAnnotation(tag = signUpTxt, annotation = signUpTxt)
                    append(signUpTxt)
                }
            }

            ClickableText(
                text = annotatedString,
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF000000)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                onClick = { offset ->
                    annotatedString.getStringAnnotations(offset, offset).firstOrNull()
                        ?.also { span ->
                            if ((span.item == signUpTxt)) {
                                //TODO Navegar al registro
                                go(Routes.SignUpScreen)
                            }
                        }
                }
            )
        }
    }

}
