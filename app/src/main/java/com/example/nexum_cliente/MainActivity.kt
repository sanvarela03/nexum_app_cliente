package com.example.nexum_cliente

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nexum_cliente.app.CustomerApp
import com.example.nexum_cliente.data.local.AppDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Provider
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appDatabaseProvider: Provider<AppDatabase>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate: ")
        enableEdgeToEdge()
        setContent {
            CustomerApp()
        }
        appDatabaseProvider.get()
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