package com.example.nexum_cliente.ui.presenter.requests


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 4/3/2026
 * @version 1.0
 */
sealed class RequestsEvent {
    data class Refresh(val fetchFromRemote: Boolean) : RequestsEvent()
}