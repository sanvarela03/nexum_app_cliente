package com.example.nexum_cliente.ui.presenter.mercado_pago_checkout

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.contract.remote.payload.req.NewContractReq
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.mercado_pago.remote.payload.res.CreatePreferenceResponse
import com.example.nexum_cliente.domain.use_cases.contract.ContractUseCases
import com.example.nexum_cliente.domain.use_cases.mercado_pago.MercadoPagoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MercadoPagoViewModel @Inject constructor(
    private val mercadoPagoUseCases: MercadoPagoUseCases,
    private val contractUseCases: ContractUseCases
) : ViewModel() {

    var state by mutableStateOf(MercadoPagoUiState())
        private set

    fun setCheckoutInfo(jobApplicationId: Long, agreedAmount: Double) {
        state = state.copy(
            jobApplicationId = jobApplicationId,
            agreedAmount = agreedAmount
        )
        checkExistingContract(jobApplicationId)
    }

    fun checkExistingContract(jobApplicationId: Long) {
        viewModelScope.launch {
            state = state.copy(isCheckingExistingContract = true)
            contractUseCases.getMyContracts()
                .catch { e ->
                    Log.e(TAG, "Error checking existing contracts:", e)
                    state = state.copy(isCheckingExistingContract = false)
                }
                .collect { r ->
                    when (r) {
                        ApiResponse.Loading -> {
                            state = state.copy(isCheckingExistingContract = true)
                        }
                        is ApiResponse.Success -> {
                            val existing = r.data.find { it.jobApplicationId == jobApplicationId }
                            if (existing != null) {
                                Log.d(TAG, "Existing contract found for jobAppId=$jobApplicationId: contractId=${existing.id}")
                                state = state.copy(
                                    hasExistingContract = true,
                                    createdContract = existing,
                                    isCheckingExistingContract = false
                                )
                            } else {
                                state = state.copy(isCheckingExistingContract = false)
                            }
                        }
                        is ApiResponse.Error -> {
                            state = state.copy(isCheckingExistingContract = false)
                        }
                        is ApiResponse.Failure -> {
                            state = state.copy(isCheckingExistingContract = false)
                        }
                    }
                }
        }
    }

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
        Log.d(TAG, "onDeepLinkReceived uri --> $uri")
        if (uri == null) return

        val extRef = uri.getQueryParameter("external_reference")
        if (extRef?.startsWith("LABOR_") == true) {
            Log.d(TAG, "Deep link is for Labor payment, ignoring in MercadoPagoViewModel")
            return
        }

        val statusParam = uri.getQueryParameter("status")
            ?: uri.getQueryParameter("collection_status")
        val paymentIdParam = uri.getQueryParameter("payment_id")
            ?: uri.getQueryParameter("collection_id")
            ?: ""
        val lastSegment = uri.lastPathSegment?.lowercase() ?: ""

        val isApproved = statusParam.equals("approved", ignoreCase = true) ||
                lastSegment == "success" ||
                (lastSegment == "resultado" && statusParam.equals("approved", ignoreCase = true))

        val isRejected = statusParam.equals("rejected", ignoreCase = true) ||
                statusParam.equals("cancelled", ignoreCase = true) ||
                lastSegment == "failure"

        val isPending = statusParam.equals("in_process", ignoreCase = true) ||
                statusParam.equals("pending", ignoreCase = true) ||
                lastSegment == "pending"

        val paymentTypeParam = uri.getQueryParameter("payment_type")

        val extRefRaw = uri.getQueryParameter("external_reference")
        if (!extRefRaw.isNullOrBlank()) {
            val parts = extRefRaw.split("_")
            val restoredAppId = parts.getOrNull(0)?.toLongOrNull()
            val restoredAmount = parts.getOrNull(1)?.toDoubleOrNull()
            if (restoredAppId != null) {
                Log.d(TAG, "Restored from external_reference: appId=$restoredAppId, amount=$restoredAmount")
                val finalAmount = if (state.agreedAmount != null && state.agreedAmount!! > 0) state.agreedAmount else restoredAmount
                state = state.copy(
                    jobApplicationId = state.jobApplicationId ?: restoredAppId,
                    agreedAmount = finalAmount
                )
            }
        }

        val paymentResult = when {
            isApproved -> PaymentResult.Success(paymentId = paymentIdParam)
            isRejected -> PaymentResult.Failure
            isPending -> PaymentResult.Pending
            else -> when (lastSegment) {
                "success" -> PaymentResult.Success(paymentId = paymentIdParam)
                "failure" -> PaymentResult.Failure
                "pending" -> PaymentResult.Pending
                else -> null
            }
        }

        Log.d(TAG, "Parsed paymentResult: $paymentResult (paymentId: $paymentIdParam, status: $statusParam, jobAppId: ${state.jobApplicationId}, paymentType: $paymentTypeParam)")

        if (paymentResult != null) {
            state = state.copy(
                paymentResult = paymentResult,
                paymentType = paymentTypeParam
            )
            if (paymentResult is PaymentResult.Success && !state.isContractEstablished && !state.isCreatingContract) {
                createContract()
            }
        }
    }

    fun createContract() {
        if (state.isCreatingContract || state.hasExistingContract || (state.createdContract != null && state.createdContract!!.id > 0)) {
            Log.w(TAG, "createContract BLOCKED: contract already exists or is being created!")
            return
        }
        val appId = state.jobApplicationId
        val amount = state.agreedAmount ?: 0.0
        val paymentId = when (val res = state.paymentResult) {
            is PaymentResult.Success -> res.paymentId.toLongOrNull()
            else -> null
        }
        val feeAmount = state.price.toDoubleOrNull() ?: 8700.0

        Log.d(TAG, "createContract called: jobApplicationId=$appId, agreedAmount=$amount, externalPaymentId=$paymentId, feeAmount=$feeAmount")
        if (appId == null) {
            Log.w(TAG, "createContract aborted: jobApplicationId is null!")
            return
        }
        viewModelScope.launch {
            Log.d(TAG, "Sending createContract request to backend for appId=$appId, paymentId=$paymentId...")
            contractUseCases.createContract(
                NewContractReq(
                    jobApplicationId = appId,
                    formOfPaymentId = 1L,
                    agreedAmount = amount,
                    currencyCode = "COP", // TODO: Cambiar en funcion del phone code
                    externalPaymentId = paymentId,
                    paymentAmount = feeAmount,
                    paymentMethod = state.paymentType ?: "credit_card",
                    paymentProvider = "MERCADO_PAGO"
                )
            )
                .catch { e ->
                    Log.e(TAG, "Exception creating contract:", e)
                    state = state.copy(
                        isCreatingContract = false,
                        error = e.message ?: "Error al formalizar el contrato"
                    )
                }
                .collect { r ->
                    Log.d(TAG, "createContract response: $r")
                    when (r) {
                        ApiResponse.Loading -> {
                            Log.d(TAG, "createContract: Loading...")
                            state = state.copy(isCreatingContract = true)
                        }

                        is ApiResponse.Success -> {
                            Log.d(TAG, "createContract: SUCCESS! Contract ID = ${r.data.id}")
                            state = state.copy(
                                isCreatingContract = false,
                                createdContract = r.data
                            )
                        }

                        is ApiResponse.Error -> {
                            Log.e(TAG, "createContract: Error - ${r.errorMessage}")
                            state = state.copy(
                                isCreatingContract = false,
                                error = r.errorMessage
                            )
                        }

                        is ApiResponse.Failure -> {
                            Log.e(TAG, "createContract: Failure (${r.code}) - ${r.errorMessage}")
                            state = state.copy(
                                isCreatingContract = false,
                                error = r.errorMessage
                            )
                        }
                    }
                }
        }
    }

    fun resetPaymentResult() {
        state = state.copy(paymentResult = null)
    }

    fun resetInitPoint() {
        state = state.copy(initPoint = null)
    }


    fun createPreference() {
        Log.d(TAG, "CLICK createPreference")
        if (state.isLoading || state.isCreatingContract || state.isCheckingExistingContract || state.isPaymentOrContractDone) {
            Log.w(TAG, "createPreference BLOCKED: already processing, contract exists, or payment completed!")
            return
        }
        viewModelScope.launch {
            mercadoPagoUseCases.createPreference(state.toReq())
                .catch { e ->
                    state = state.copy(error = e.message, isLoading = false)
                    Log.e("MercadoPagoViewModel", "Error creating preference:", e)
                }
                .collect { r ->
                    Log.d(TAG, "Resultado del pago --> ${r}")
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

    companion object {
        private val TAG = "MercadoPagoViewModel"
    }
}