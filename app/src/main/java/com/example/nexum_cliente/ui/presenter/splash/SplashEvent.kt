package com.example.nexum_cliente.ui.presenter.splash

sealed class SplashEvent {
    object CheckAuthentication : SplashEvent()
}