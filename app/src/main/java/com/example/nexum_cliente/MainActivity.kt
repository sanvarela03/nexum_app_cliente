package com.example.nexum_cliente

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import com.example.nexum_cliente.app.CustomerApp
import com.example.nexum_cliente.data.local.AppDatabase
import com.example.nexum_cliente.ui.global_viewmodels.AuthViewModel
import com.example.nexum_cliente.ui.presenter.mercado_pago_checkout.MercadoPagoViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appDatabaseProvider: Provider<AppDatabase>
    private val authViewModel: AuthViewModel by viewModels()
    private val mercadoPagoViewModel: MercadoPagoViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate: ")

        // Desactivar el ajuste automático de la ventana por parte del sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)

        mercadoPagoViewModel.onDeepLinkReceived(intent?.data)

        enableEdgeToEdge()
        setContent {
            val userAuthState = authViewModel.isAuthenticated.collectAsState().value
            val destination = authViewModel.startDestinationData.collectAsState().value

            LaunchedEffect(destination) { }
            Log.d("MainActivity", "userAuthState: $userAuthState")

            CustomerApp(
                userAuthState = userAuthState,
                startDestination = destination,
             )
        }
//        appDatabaseProvider.get()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mercadoPagoViewModel.onDeepLinkReceived(intent.data)
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("MainActivity", "onRestart: ")
    }
}