package com.example.neuxum_cliente.ui.presenter.splash

sealed class SplashEvent {
    object CheckAuthentication : SplashEvent()
}