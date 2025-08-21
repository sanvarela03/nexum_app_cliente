package com.example.neuxum_cliente.ui.presenter.sign_up.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 20/08/2025
 * @version 1.0
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignUpUploadDocumentScreen (

) {
    Column(){
        OutlinedButton(
            onClick = { /*TODO*/ },
        ) {
            Text(
                "Frente del documento"
            )
        }
        OutlinedButton(
            onClick = { /*TODO*/ },
            ) {
            Text(
                "Dorso del documento"
            )
        }
    }
}