package com.example.neuxum_cliente.ui.presenter.categories

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neuxum_cliente.data.global_payload.res.ApiResponse
import com.example.neuxum_cliente.domain.use_cases.category.CategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/5/2025
 * @version 1.0
 */
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    var state by mutableStateOf(CategoriesState())

    private var updateCategoriesJob: Job? = null

    init {
        observeCategories()
        updateCategories(fetchFromRemote = true)
    }

    fun onEvent(event: CategoriesEvent) {
        when (event) {
            is CategoriesEvent.Refresh -> {
                updateCategories(fetchFromRemote = true)
            }
        }

    }

    fun observeCategories() {
        viewModelScope.launch {
            categoryUseCases.observeCategories()
                .distinctUntilChanged()
                .catch { e ->
                    Log.d("CategoriesViewModel", "Error observando categorías: ${e.message}")
                    state = state.copy(errorMessage = e.message ?: "Unknown error")
                }
                .collect { categories ->
                    state = state.copy(categories = categories)
                }

        }
    }

    fun updateCategories(fetchFromRemote: Boolean = false) {
        Log.d(
            "CategoriesViewModel",
            "Actualizando categorias: fetchFromRemote=$fetchFromRemote"
        )

        updateCategoriesJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        updateCategoriesJob = viewModelScope.launch {
            categoryUseCases.updateCategories(fetchFromRemote).catch {
                Log.d("CategoriesViewModel", "Error actualizando categorias: ${it.message}")
                state = state.copy(errorMessage = it.message ?: "Unknown error")
            }.collect {
                when (it) {
                    is ApiResponse.Error -> {
                        Log.d(
                            "CategoriesViewModel",
                            "Error actualizando categorias: ${it.errorMessage}"
                        )
                        state = state.copy(isRefreshing = false)
                        state = state.copy(errorMessage = it.errorMessage)
                        Log.d(
                            "CategoriesViewModel",
                            "Error actualizando categorias: ${state.errorMessage}"
                        )
                        Log.d(
                            "CategoriesViewModel",
                            "Error actualizando isNotEmpty: ${state.errorMessage.isNotEmpty()}"
                        )
                    }

                    is ApiResponse.Failure -> {
                        state = state.copy(isRefreshing = false)
                    }

                    ApiResponse.Loading -> {
                        state = state.copy(isRefreshing = true)
                    }

                    is ApiResponse.Success -> {
                        state = state.copy(isRefreshing = false)

                    }
                }
            }
        }
    }

    override fun onCleared() {
        Log.d("CategoriesViewModel", "onCleared")
        super.onCleared()

        updateCategoriesJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }

    }

}