package com.example.nexum_cliente.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.nexum_cliente.MainActivity
import com.example.nexum_cliente.R
import com.example.nexum_cliente.data.notification.local.NotificationEntity
import com.example.nexum_cliente.domain.use_cases.notification.NotificationUseCases
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.messaging
import com.google.firebase.messaging.remoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationsUseCases: NotificationUseCases

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.v(TAG, "MyFirebaseMessagingService")
        Log.v(TAG, message.notification?.title ?: "No tiene titulo")
        Log.v(TAG, message.notification?.body ?: "No tiene cuerpo")

        message.data.forEach { (key, value) ->
            Log.v(TAG, "| Llave : $key | Valor: $value |")
        }

        val title = message.notification?.title ?: message.data["title"]
        val body = message.notification?.body ?: message.data["body"]

        if (title != null && body != null) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")
            val formatted = current.format(formatter)
            val notification = NotificationEntity(null, title, body, formatted)
            CoroutineScope(Dispatchers.IO).launch {
                notificationsUseCases.saveNotification(notification)
            }
        }
        showNotification(message, title, body)
    }


    override fun onNewToken(token: String) {
        Log.v(TAG, "Token: $token")
        super.onNewToken(token)
//        CoroutineScope(Dispatchers.IO).launch {
//            producerUseCases.updateFirebaseToken(token)
//        }
    }

    private fun showNotification(message: RemoteMessage, title: String?, body: String?) {
        val channelId = "Default"
        val channelName = "Default Channel"

        // Crear un NotificationManager
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear un canal de notificación (necesario para Android Oreo y versiones posteriores)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Intentar abrir tu actividad cuando se haga clic en la notificación
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        
        // Extraer payload si viene del backend
        message.data["type"]?.let { intent.putExtra("type", it) }
        message.data["jobOfferUuid"]?.let { intent.putExtra("jobOfferUuid", it) }
        message.data["jobOfferId"]?.let { intent.putExtra("jobOfferId", it) }

        // Crear PendingIntent para la actividad principal
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intentar realizar alguna acción cuando se hagan clic en los botones
        val acceptIntent = Intent(this, AcceptBroadcastReceiver::class.java)
        val acceptPendingIntent = PendingIntent.getBroadcast(
            this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val rejectIntent = Intent(this, RejectBroadcastReceiver::class.java)
        val rejectPendingIntent = PendingIntent.getBroadcast(
            this, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construir la notificación con botones
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title ?: "No tiene titulo")
            .setContentText(body ?: "No tiene cuerpo")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)


        // Mostrar la notificación
        notificationManager.notify(0, notificationBuilder.build())
    }

    fun sendNotification(title: String, message: String, key: String) {
        val to = key
        val msgId = AtomicInteger()

        val fm = Firebase.messaging

        fm.send(
            remoteMessage(to = to) {
                setMessageId(msgId.get().toString())
                addData("title", title)
                addData("message", message)
            },
        )
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }
}