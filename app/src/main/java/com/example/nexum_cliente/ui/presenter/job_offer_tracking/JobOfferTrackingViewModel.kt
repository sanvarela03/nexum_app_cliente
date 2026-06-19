package com.example.nexum_cliente.ui.presenter.job_offer_tracking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.use_cases.job_offer.JobOfferUseCases
import com.example.nexum_cliente.domain.use_cases.nearby_workers.NearbyWorkersUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobOfferTrackingViewModel @Inject constructor(
    private val nearbyWorkersUseCases: NearbyWorkersUseCases,
    private val jobOfferUseCases: JobOfferUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(JobOfferTrackingState())
    val state = _state.asStateFlow()

    private val jobOfferUuid: String? = savedStateHandle.get<String>("jobOfferId")
    private var jobOfferId: Long? = null

    init {
        if (jobOfferUuid != null) {
            viewModelScope.launch {
                val offer = jobOfferUseCases.getJobOfferByUuid(jobOfferUuid)
                if (offer != null) {
                    jobOfferId = offer.id
                    observeWorkers()
                    updateWorkers()
                } else {
                    _state.update { it.copy(errorMessage = "Oferta no encontrada localmente") }
                }
            }
        } else {
            _state.update { it.copy(errorMessage = "ID de oferta inválido") }
        }
    }

    fun onEvent(event: JobOfferTrackingEvent) {
        when (event) {
            is JobOfferTrackingEvent.Refresh -> updateWorkers()
            is JobOfferTrackingEvent.ClearError -> _state.update { it.copy(errorMessage = "") }
        }
    }

    private fun observeWorkers() {
        val currentId = jobOfferId ?: return
        viewModelScope.launch {
            nearbyWorkersUseCases.observeNearbyWorkers(currentId).collect { workers ->
                _state.update { it.copy(workers = workers) }
            }
        }
    }

    private fun updateWorkers() {
        val currentId = jobOfferId ?: return
        viewModelScope.launch {
            nearbyWorkersUseCases.updateNearbyWorkers(
                jobOfferId = currentId,
                radiusKm = 10,
                fetchFromRemote = true
            ).collect { response ->
                when (response) {
                    is ApiResponse.Success -> _state.update { it.copy(isRefreshing = false) }
                    is ApiResponse.Error -> _state.update {
                        it.copy(isRefreshing = false, errorMessage = response.errorMessage)
                    }
                    is ApiResponse.Failure -> _state.update {
                        it.copy(isRefreshing = false, errorMessage = response.errorMessage)
                    }
                    ApiResponse.Loading -> _state.update { it.copy(isRefreshing = true) }
                }
            }
        }
    }

    companion object {
        private const val TAG = "JobOfferTrackingViewModel"
    }
}