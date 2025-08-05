package com.example.neuxum_cliente.ui.presenter.home

import com.example.neuxum_cliente.data.client.local.ClientEntity


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/1/2025
 * @version 1.0
 */
data class HomeState(
    val clientEntity: ClientEntity? = null,
    var customerInfo: String = "",
    val isRefreshing: Boolean = false,
    val errorMessage: String = ""
)