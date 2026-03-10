package com.example.nexum_cliente.ui.presenter.mercado_pago_checkout

import android.net.Uri


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/28/2026
 * @version 1.0
 */
sealed class MercadoPagoEvent {
    data class TitleChanged(val title: String) : MercadoPagoEvent()
    data class PriceChanged(val price: String) : MercadoPagoEvent()
    data class QuantityChanged(val quantity: String) : MercadoPagoEvent()
//    data class OnDeepLinkReceived(val uri: Uri?) : MercadoPagoEvent()

}