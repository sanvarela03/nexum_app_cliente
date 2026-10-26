package com.example.nexum_cliente.ui.presenter.contracts

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.contract.remote.payload.req.PayLaborReq
import com.example.nexum_cliente.data.contract.remote.payload.res.ContractRes
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.mercado_pago.remote.payload.req.CreatePreferenceRequest
import com.example.nexum_cliente.data.review.remote.payload.req.CreateReviewReq
import com.example.nexum_cliente.domain.use_cases.contract.ContractUseCases
import com.example.nexum_cliente.domain.use_cases.mercado_pago.MercadoPagoUseCases
import com.example.nexum_cliente.domain.use_cases.review.ReviewUseCases
import com.example.nexum_cliente.utils.PaymentDeepLinkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
@HiltViewModel
class ContractsViewModel @Inject constructor(
    private val contractUseCases: ContractUseCases,
    private val reviewUseCases: ReviewUseCases,
    private val mercadoPagoUseCases: MercadoPagoUseCases,
    private val paymentDeepLinkManager: PaymentDeepLinkManager
) : ViewModel() {

    var state by mutableStateOf(ContractsState())
        private set

    init {
        onEvent(ContractsEvent.LoadContracts)
        observePaymentDeepLinks()
    }

    fun onEvent(event: ContractsEvent) {
        when (event) {
            is ContractsEvent.LoadContracts -> loadContracts(isRefreshing = false)
            is ContractsEvent.RefreshContracts -> loadContracts(isRefreshing = true)
            is ContractsEvent.SelectFilter -> {
                state = state.copy(selectedFilter = event.filter)
            }
            is ContractsEvent.GenerateVerificationCode -> {
                generateCode(event.contract)
            }
            is ContractsEvent.DismissCodeDialog -> {
                state = state.copy(showCodeDialog = false, generatedCode = null, selectedContract = null)
            }
            is ContractsEvent.OpenPaymentDialog -> {
                state = state.copy(showPaymentDialog = true, contractToPay = event.contract)
            }
            is ContractsEvent.DismissPaymentDialog -> {
                state = state.copy(showPaymentDialog = false, contractToPay = null)
            }
            is ContractsEvent.ConfirmPayLabor -> {
                payLabor(event.contract, event.paymentMethod)
            }
            is ContractsEvent.OpenReviewDialog -> {
                state = state.copy(showReviewDialog = true, contractToReview = event.contract)
            }
            is ContractsEvent.DismissReviewDialog -> {
                state = state.copy(showReviewDialog = false, contractToReview = null)
            }
            is ContractsEvent.SubmitReview -> {
                submitReview(event.rating, event.comment)
            }
            is ContractsEvent.ResetInitPoint -> {
                state = state.copy(initPoint = null)
            }
            is ContractsEvent.ClearMessages -> {
                state = state.copy(errorMessage = null, successMessage = null)
            }
        }
    }

    private fun observePaymentDeepLinks() {
        viewModelScope.launch {
            paymentDeepLinkManager.deepLinkFlow.collect { uri ->
                handlePaymentDeepLink(uri)
            }
        }
    }

    private fun handlePaymentDeepLink(uri: Uri) {
        val extRef = uri.getQueryParameter("external_reference") ?: return
        if (!extRef.startsWith("LABOR_")) return

        Log.d(TAG, "Deep link received for labor payment: $uri")

        val parts = extRef.split("_")
        // Format: LABOR_{contractId}_{amount}
        val contractId = parts.getOrNull(1)?.toLongOrNull() ?: return
        val extAmount = parts.getOrNull(2)?.toDoubleOrNull() ?: 0.0

        val contract = state.contracts.find { it.id == contractId }
        val finalAmount = if (contract != null && contract.agreedAmount > 0) contract.agreedAmount else extAmount

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

        val paymentType = uri.getQueryParameter("payment_type") ?: "credit_card"

        Log.d(TAG, "Labor payment parsed: approved=$isApproved, rejected=$isRejected, pending=$isPending, paymentId=$paymentIdParam, contractId=$contractId")

        when {
            isApproved -> {
                confirmLaborPayment(
                    contractId = contractId,
                    amount = finalAmount,
                    paymentId = paymentIdParam,
                    paymentMethod = paymentType
                )
            }
            isRejected -> {
                state = state.copy(
                    isPayingLabor = false,
                    errorMessage = "El pago en Mercado Pago fue rechazado o cancelado."
                )
            }
            isPending -> {
                state = state.copy(
                    isPayingLabor = false,
                    errorMessage = "El pago está en proceso de acreditación en Mercado Pago."
                )
            }
        }
    }

    private fun loadContracts(isRefreshing: Boolean) {
        viewModelScope.launch {
            contractUseCases.getMyContracts()
                .catch { e ->
                    state = state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = e.message ?: "Error al cargar contratos"
                    )
                }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            state = state.copy(
                                isLoading = !isRefreshing,
                                isRefreshing = isRefreshing
                            )
                        }
                        is ApiResponse.Success -> {
                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                contracts = response.data
                            )
                        }
                        is ApiResponse.Error -> {
                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = response.errorMessage
                            )
                        }
                        is ApiResponse.Failure -> {
                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = response.errorMessage
                            )
                        }
                    }
                }
        }
    }

    private fun generateCode(contract: ContractRes) {
        viewModelScope.launch {
            contractUseCases.generateVerificationCode(contract.id)
                .catch { e ->
                    state = state.copy(
                        isGeneratingCode = false,
                        errorMessage = e.message ?: "Error al generar código"
                    )
                }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            state = state.copy(isGeneratingCode = true)
                        }
                        is ApiResponse.Success -> {
                            val code = response.data["code"] ?: response.data.values.firstOrNull() ?: "000000"
                            state = state.copy(
                                isGeneratingCode = false,
                                generatedCode = code,
                                selectedContract = contract,
                                showCodeDialog = true
                            )
                        }
                        is ApiResponse.Error -> {
                            state = state.copy(
                                isGeneratingCode = false,
                                errorMessage = response.errorMessage
                            )
                        }
                        is ApiResponse.Failure -> {
                            state = state.copy(
                                isGeneratingCode = false,
                                errorMessage = response.errorMessage
                            )
                        }
                    }
                }
        }
    }

    private fun submitReview(rating: Int, comment: String) {
        val contract = state.contractToReview ?: return
        viewModelScope.launch {
            val req = CreateReviewReq(
                contractId = contract.id,
                rating = rating,
                comment = comment.takeIf { it.isNotBlank() }
            )
            reviewUseCases.createReview(req)
                .catch { e ->
                    state = state.copy(
                        isSubmittingReview = false,
                        errorMessage = e.message ?: "Error al enviar calificación"
                    )
                }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            state = state.copy(isSubmittingReview = true)
                        }
                        is ApiResponse.Success -> {
                            state = state.copy(
                                isSubmittingReview = false,
                                showReviewDialog = false,
                                contractToReview = null,
                                successMessage = "¡Calificación enviada exitosamente! Gracias por tu opinión."
                            )
                            loadContracts(isRefreshing = true)
                        }
                        is ApiResponse.Error -> {
                            state = state.copy(
                                isSubmittingReview = false,
                                errorMessage = response.errorMessage
                            )
                        }
                        is ApiResponse.Failure -> {
                            state = state.copy(
                                isSubmittingReview = false,
                                errorMessage = response.errorMessage
                            )
                        }
                    }
                }
        }
    }

    private fun payLabor(contract: ContractRes, paymentMethod: String) {
        viewModelScope.launch {
            state = state.copy(isPayingLabor = true)
            val amount = contract.agreedAmount
            val extRef = "LABOR_${contract.id}_${amount.toLong()}"
            val req = CreatePreferenceRequest(
                title = "Mano de Obra - Contrato #${contract.id}",
                quantity = 1,
                unitPrice = amount,
                externalReference = extRef
            )
            Log.d(TAG, "Creating preference for labor: $req")
            mercadoPagoUseCases.createPreference(req)
                .catch { e ->
                    Log.e(TAG, "Error al crear preferencia MP", e)
                    state = state.copy(
                        isPayingLabor = false,
                        errorMessage = e.message ?: "Error al conectar con Mercado Pago"
                    )
                }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            state = state.copy(isPayingLabor = true)
                        }
                        is ApiResponse.Success -> {
                            val initPoint = response.data.initPoint
                            Log.d(TAG, "Preference created successfully! initPoint=$initPoint")
                            state = state.copy(
                                isPayingLabor = false,
                                showPaymentDialog = false,
                                initPoint = initPoint
                            )
                        }
                        is ApiResponse.Error -> {
                            state = state.copy(
                                isPayingLabor = false,
                                errorMessage = response.errorMessage
                            )
                        }
                        is ApiResponse.Failure -> {
                            state = state.copy(
                                isPayingLabor = false,
                                errorMessage = response.errorMessage
                            )
                        }
                    }
                }
        }
    }

    private fun confirmLaborPayment(
        contractId: Long,
        amount: Double,
        paymentId: String,
        paymentMethod: String
    ) {
        viewModelScope.launch {
            Log.d(TAG, "Confirming labor payment in backend: contractId=$contractId, amount=$amount, paymentId=$paymentId")
            state = state.copy(isPayingLabor = true)
            val req = PayLaborReq(
                externalPaymentId = paymentId.toLongOrNull() ?: System.currentTimeMillis(),
                paymentAmount = amount,
                paymentMethod = paymentMethod,
                paymentProvider = "MERCADO_PAGO"
            )
            contractUseCases.payLabor(contractId, req)
                .catch { e ->
                    Log.e(TAG, "Exception confirming labor payment:", e)
                    state = state.copy(
                        isPayingLabor = false,
                        errorMessage = e.message ?: "Error al confirmar el pago en el servidor"
                    )
                }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            state = state.copy(isPayingLabor = true)
                        }
                        is ApiResponse.Success -> {
                            state = state.copy(
                                isPayingLabor = false,
                                contractToPay = null,
                                successMessage = "¡Pago de mano de obra confirmado exitosamente! Fondos en custodia segura y trabajo activado."
                            )
                            loadContracts(isRefreshing = true)
                        }
                        is ApiResponse.Error -> {
                            state = state.copy(
                                isPayingLabor = false,
                                errorMessage = response.errorMessage
                            )
                        }
                        is ApiResponse.Failure -> {
                            state = state.copy(
                                isPayingLabor = false,
                                errorMessage = response.errorMessage
                            )
                        }
                    }
                }
        }
    }

    companion object {
        private const val TAG = "ContractsViewModel"
    }
}
