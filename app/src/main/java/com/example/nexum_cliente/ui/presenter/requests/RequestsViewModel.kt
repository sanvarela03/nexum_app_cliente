package com.example.nexum_cliente.ui.presenter.requests

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.use_cases.job_offer.JobOfferUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 4/3/2026
 * @version 1.0
 */
@HiltViewModel
class RequestsViewModel @Inject constructor(
    private val jobOfferUseCases: JobOfferUseCases
) : ViewModel() {

    companion object {
        private const val TAG = "RequestsViewModel"
    }


    var state by mutableStateOf(RequestsState())
        private set

    val jobOffers = jobOfferUseCases.observeJobOffers()

    init {
        state = state.copy(isLoading = true)
        observe()
        update(fetchFromRemote = false)
    }

    fun onEvent(event: RequestsEvent) {
        when (event) {
            is RequestsEvent.Refresh -> {
                update(event.fetchFromRemote)
            }
        }

    }

    private fun observe() {
        viewModelScope.launch {
            jobOffers
                .distinctUntilChanged()
                .catch { e ->
                    state = state.copy(errorMessage = e.message ?: "Unknown error")
                }
                .collect { jobOffers ->
                    state = state.copy(jobOffers = jobOffers)
                }
        }
    }

    private fun update(fetchFromRemote: Boolean = false) {
        viewModelScope.launch {
            jobOfferUseCases.updateJobOffers(fetchFromRemote)
                .catch { e ->
                    Log.e(TAG, "Error updating job offers: ${e.message}")
                    state = state.copy(errorMessage = e.message ?: "Unknown error")
                }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            state = state.copy(isLoading = false, isRefreshing = false)
                        }

                        is ApiResponse.Error -> {
                            state = state.copy(isLoading = false, isRefreshing = false)
                        }

                        is ApiResponse.Failure -> {
                            state = state.copy(isLoading = false, isRefreshing = false)

                        }

                        is ApiResponse.Loading -> {
                            state = state.copy(isLoading = true, isRefreshing = true)
                        }
                    }
                }
        }
    }
}