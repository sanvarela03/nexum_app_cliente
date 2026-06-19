package com.example.nexum_cliente.ui.presenter.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.domain.use_cases.notification.NotificationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationsUseCases: NotificationUseCases
) : ViewModel() {
    val notifications = notificationsUseCases.getNotifications()

    fun onEvent(event: NotificationsEvent) {
        when (event) {
            is NotificationsEvent.DeleteBtnClick -> {
                deleteNotification(event.id)
            }
        }
    }

    private fun deleteNotification(id: Int) {
        viewModelScope.launch {
            notificationsUseCases.deleteNotification(id)
        }
    }
}