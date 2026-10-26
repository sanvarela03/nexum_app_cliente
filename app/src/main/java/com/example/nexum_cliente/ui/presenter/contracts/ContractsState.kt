package com.example.nexum_cliente.ui.presenter.contracts

import com.example.nexum_cliente.data.contract.remote.payload.res.ContractRes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
data class ContractsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val contracts: List<ContractRes> = emptyList(),
    val selectedFilter: String = "ALL",
    val selectedContract: ContractRes? = null,
    val generatedCode: String? = null,
    val isGeneratingCode: Boolean = false,
    val showCodeDialog: Boolean = false,
    val showPaymentDialog: Boolean = false,
    val contractToPay: ContractRes? = null,
    val isPayingLabor: Boolean = false,
    val showReviewDialog: Boolean = false,
    val contractToReview: ContractRes? = null,
    val isSubmittingReview: Boolean = false,
    val initPoint: String? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
