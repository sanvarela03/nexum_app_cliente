package com.example.neuxum_cliente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.neuxum_cliente.app.CustomerApp
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpScreen
import com.example.neuxum_cliente.ui.presenter.sign_up.SignUpUserDataScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomerApp()
            //SignUpUserDataScreen()
        }
    }
}