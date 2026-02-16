package com.example.nexum_cliente.ui.presenter.home

import com.example.nexum_cliente.data.client.local.ClientEntity
import com.example.nexum_cliente.domain.model.Client


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/1/2025
 * @version 1.0
 */
data class HomeState(
    val clientEntity: ClientEntity? = null,
    val client: Client? = null,
    var customerInfo: String = "",
    val isRefreshing: Boolean = false,
    var errorMessage: String = "",
    val isSignOutLoading: Boolean = false,
    val signOutResponse : String = ""
)