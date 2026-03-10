package com.example.nexum_cliente.ui.presenter.mercado_pago_checkout

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.mercado_pago.remote.payload.res.CreatePreferenceResponse
import com.example.nexum_cliente.domain.use_cases.mercado_pago.MercadoPagoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MercadoPagoViewModel @Inject constructor(
    private val mercadoPagoUseCases: MercadoPagoUseCases
) : ViewModel() {

    var state by mutableStateOf(MercadoPagoUiState())
        private set

    fun onEvent(event: MercadoPagoEvent) {
        state = when (event) {
            is MercadoPagoEvent.TitleChanged -> {
                state.copy(title = event.title)
            }

            is MercadoPagoEvent.PriceChanged -> {
                state.copy(price = event.price)
            }

            is MercadoPagoEvent.QuantityChanged -> {
                state.copy(quantity = event.quantity)
            }
        }
    }

    fun onDeepLinkReceived(uri: Uri?) {
        if (uri == null) return
        val paymentResult = when (uri.lastPathSegment) {
            "success" -> PaymentResult.Success(
                paymentId = uri.getQueryParameter("payment_id") ?: ""
            )

            "failure" -> PaymentResult.Failure
            "pending" -> PaymentResult.Pending
            else -> null
        }
        state = state.copy(paymentResult = paymentResult)
    }

    fun resetPaymentResult() {
        state = state.copy(paymentResult = null)
    }

    fun resetInitPoint() {
        state = state.copy(initPoint = null)
    }


    fun createPreference() {
        viewModelScope.launch {
            mercadoPagoUseCases.createPreference(state.toReq())
                .catch { e ->
                    state = state.copy(error = e.message, isLoading = false)
                    Log.e("MercadoPagoViewModel", "Error creating preference:", e)
                }
                .collect { r ->
                    when (r) {
                        is ApiResponse.Error -> {
                            state = state.copy(error = r.errorMessage, isLoading = false)
                        }

                        is ApiResponse.Failure -> {
                            state = state.copy(error = r.errorMessage, isLoading = false)
                        }

                        ApiResponse.Loading -> {
                            state = state.copy(isLoading = true)
                        }

                        is ApiResponse.Success -> {
                            val data: CreatePreferenceResponse = r.data
                            state = state.copy(
                                preferenceId = data.preferenceId,
                                initPoint = data.initPoint,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }
}