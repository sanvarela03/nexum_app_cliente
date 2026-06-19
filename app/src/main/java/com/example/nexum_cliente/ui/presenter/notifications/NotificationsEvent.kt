package com.example.nexum_cliente.ui.presenter.notifications

sealed class NotificationsEvent {
    data class DeleteBtnClick(val id: Int) : NotificationsEvent()
}