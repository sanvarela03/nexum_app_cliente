package com.example.neuxum_cliente.ui.presenter.home


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/1/2025
 * @version 1.0
 */
sealed class HomeEvent {
    object SignOutBtnClicked : HomeEvent()
    object Refresh : HomeEvent()
}
