package com.example.nexum_cliente.utils

import android.net.Uri
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor singleton para distribuir deep links de pagos (Mercado Pago u otros proveedores)
 * a los ViewModels correspondientes.
 */
@Singleton
class PaymentDeepLinkManager @Inject constructor() {

    private val _deepLinkFlow = MutableSharedFlow<Uri>(
        replay = 1,
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val deepLinkFlow = _deepLinkFlow.asSharedFlow()

    fun onDeepLinkReceived(uri: Uri?) {
        if (uri != null) {
            _deepLinkFlow.tryEmit(uri)
        }
    }
}
