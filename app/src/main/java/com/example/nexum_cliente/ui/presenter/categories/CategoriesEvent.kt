package com.example.nexum_cliente.ui.presenter.categories

sealed class CategoriesEvent {
    object Refresh : CategoriesEvent()
    object ClearError : CategoriesEvent()
}
