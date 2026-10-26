package com.example.nexum_cliente.ui.presenter.contracts

import com.example.nexum_cliente.data.contract.remote.payload.res.ContractRes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
sealed class ContractsEvent {
    object LoadContracts : ContractsEvent()
    object RefreshContracts : ContractsEvent()
    data class SelectFilter(val filter: String) : ContractsEvent()
    data class GenerateVerificationCode(val contract: ContractRes) : ContractsEvent()
    object DismissCodeDialog : ContractsEvent()
    data class OpenPaymentDialog(val contract: ContractRes) : ContractsEvent()
    object DismissPaymentDialog : ContractsEvent()
    data class ConfirmPayLabor(val contract: ContractRes, val paymentMethod: String = "credit_card") : ContractsEvent()
    data class OpenReviewDialog(val contract: ContractRes) : ContractsEvent()
    object DismissReviewDialog : ContractsEvent()
    data class SubmitReview(val rating: Int, val comment: String) : ContractsEvent()
    object ResetInitPoint : ContractsEvent()
    object ClearMessages : ContractsEvent()
}
