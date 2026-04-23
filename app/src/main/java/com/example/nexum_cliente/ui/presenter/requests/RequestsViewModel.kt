package com.example.nexum_cliente.ui.presenter.requests

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.use_cases.job_offer.JobOfferUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
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

    private val _state = MutableStateFlow(RequestsState())
    val state = _state.asStateFlow()

    private val observeJobOffers = jobOfferUseCases.observeJobOffers()

    init {
        _state.update { it.copy(isLoading = true) }
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
            observeJobOffers
                .distinctUntilChanged()
                .catch { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Unknown error"
                        )
                    }
                }
                .collect { jobOffers ->
                    _state.update { it.copy(jobOffers = jobOffers) }
                }
        }
    }

    private fun update(fetchFromRemote: Boolean = false) {
        Log.d(TAG, "Updating job offers...")
        Log.d(TAG, "Fetch from remote: $fetchFromRemote")
        viewModelScope.launch {
            jobOfferUseCases.updateJobOffers(fetchFromRemote)
                .catch { e ->
                    Log.e(TAG, "Error updating job offers: ${e.message}")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = e.message ?: "Unknown error"
                        )
                    }
                }
                .collect { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            _state.update {
                                it.copy(
                                    isLoading = !fetchFromRemote,
                                    isRefreshing = fetchFromRemote
                                )
                            }
                        }

                        is ApiResponse.Success -> {
                            _state.update { it.copy(isLoading = false, isRefreshing = false) }
                        }

                        is ApiResponse.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    errorMessage = response.errorMessage
                                )
                            }
                        }

                        is ApiResponse.Failure -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    errorMessage = response.errorMessage
                                )
                            }
                        }
                    }
                }
        }
    }
}