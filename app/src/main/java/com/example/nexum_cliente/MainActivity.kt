package com.example.nexum_cliente

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    @Inject
    lateinit var updateFcmTokenUseCase: com.example.nexum_cliente.domain.use_cases.user.UpdateFcmTokenUseCase

    @Inject
    lateinit var tokenManager: com.example.nexum_cliente.security.TokenManager

    @Inject
    lateinit var paymentDeepLinkManager: com.example.nexum_cliente.utils.PaymentDeepLinkManager

    private val authViewModel: AuthViewModel by viewModels()
    private val mercadoPagoViewModel: MercadoPagoViewModel by viewModels()
    private var currentIntent by mutableStateOf<Intent?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentIntent = intent
        askNotificationPermission()
        syncFcmTokenIfLoggedIn()
        Log.d("MainActivity", "onCreate: ")

        // Desactivar el ajuste automático de la ventana por parte del sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)

        intent?.data?.let { paymentDeepLinkManager.onDeepLinkReceived(it) }
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
                initialIntent = currentIntent
            )
        }
//        appDatabaseProvider.get()
    }

    private fun syncFcmTokenIfLoggedIn() {
        if (tokenManager.getAccessTokenSync() != null) {
            com.google.firebase.messaging.FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    if (!token.isNullOrBlank()) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                updateFcmTokenUseCase(token).collect { }
                            } catch (e: Exception) {
                                Log.e("MainActivity", "Error syncing FCM token on startup", e)
                            }
                        }
                    }
                }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // Si el permiso no ha sido concedido, lo solicitamos
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        currentIntent = intent
        intent.data?.let { paymentDeepLinkManager.onDeepLinkReceived(it) }
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
