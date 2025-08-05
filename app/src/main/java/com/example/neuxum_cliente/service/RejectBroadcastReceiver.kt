package com.example.neuxum_cliente.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class RejectBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.v("BroadcastReceiver", "Rechazar clickeado")

        // Agrega la lógica que deseas ejecutar cuando se hace clic en "Aceptar"
        // Por ejemplo, puedes abrir una nueva actividad, mostrar un Toast, etc.

        Toast.makeText(context, "Rechazar clickeado", Toast.LENGTH_SHORT).show()
    }
}