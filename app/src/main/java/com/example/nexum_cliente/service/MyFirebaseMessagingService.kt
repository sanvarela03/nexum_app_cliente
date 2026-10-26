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
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationsUseCases: NotificationUseCases
    @Inject
    lateinit var updateFcmTokenUseCase: com.example.nexum_cliente.domain.use_cases.user.UpdateFcmTokenUseCase
    @Inject
    lateinit var tokenManager: com.example.nexum_cliente.security.TokenManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.v(TAG, "Mensaje FCM recibido")

        message.data.forEach { (key, value) ->
            Log.v(TAG, "| Llave : $key | Valor: $value |")
        }

        val notification = NotificationDataMapper.toNotificationEntity(message)

        if (notification != null) {
            CoroutineScope(Dispatchers.IO).launch {
                notificationsUseCases.saveNotification(notification)
            }
        }

        showNotification(message, notification?.title, notification?.body)
    }


    override fun onNewToken(token: String) {
        Log.v(TAG, "Token: $token")
        super.onNewToken(token)
        if (tokenManager.getAccessTokenSync() != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    updateFcmTokenUseCase(token).collect { }
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating FCM token", e)
                }
            }
        }
    }

    private fun showNotification(message: RemoteMessage, title: String?, body: String?) {
        val channelId = "Default"
        val channelName = "Default Channel"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = message.data["contractId"]?.toIntOrNull()
            ?: message.data["jobApplicationId"]?.toIntOrNull()
            ?: message.data["jobOfferId"]?.toIntOrNull()
            ?: (System.currentTimeMillis() % 100000).toInt()

        // Intentar abrir MainActivity cuando se haga clic en la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            message.data["type"]?.let { putExtra("type", it) }
            message.data["jobOfferUuid"]?.let { putExtra("jobOfferUuid", it) }
            message.data["jobOfferId"]?.let { putExtra("jobOfferId", it) }
            message.data["jobApplicationId"]?.let { putExtra("jobApplicationId", it) }
            message.data["contractId"]?.let { putExtra("contractId", it) }
        }

        // Crear PendingIntent para la actividad principal
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val acceptIntent = Intent(this, AcceptBroadcastReceiver::class.java)
        val acceptPendingIntent = PendingIntent.getBroadcast(
            this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val rejectIntent = Intent(this, RejectBroadcastReceiver::class.java)
        val rejectPendingIntent = PendingIntent.getBroadcast(
            this, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title ?: "No tiene titulo")
            .setContentText(body ?: "No tiene cuerpo")
            .setSmallIcon(R.drawable.nexum_logo_2)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, notificationBuilder.build())
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
