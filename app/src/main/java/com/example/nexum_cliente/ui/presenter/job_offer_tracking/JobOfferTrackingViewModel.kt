package com.example.nexum_cliente.ui.presenter.job_offer_tracking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.domain.model.JobApplication
import com.example.nexum_cliente.domain.use_cases.job_application.JobApplicationUseCases
import com.example.nexum_cliente.domain.use_cases.job_offer.JobOfferUseCases
import com.example.nexum_cliente.domain.use_cases.contract.ContractUseCases
import com.example.nexum_cliente.domain.use_cases.nearby_workers.NearbyWorkersUseCases
import com.example.nexum_cliente.domain.use_cases.review.ReviewUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobOfferTrackingViewModel @Inject constructor(
    private val nearbyWorkersUseCases: NearbyWorkersUseCases,
    private val jobOfferUseCases: JobOfferUseCases,
    private val jobApplicationUseCases: JobApplicationUseCases,
    private val reviewUseCases: ReviewUseCases,
    private val contractUseCases: ContractUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(JobOfferTrackingState())
    val state = _state.asStateFlow()

    private val jobOfferUuid: String? = savedStateHandle.get<String>("jobOfferId")
    private var jobOfferId: Long? = null
    private var pollingJob: Job? = null

    init {
        checkExistingContracts()
        if (jobOfferUuid != null) {
            observeOffer(jobOfferUuid)
            loadOfferAndWorkers(jobOfferUuid)
            startApplicationsPolling(jobOfferUuid)
        } else {
            _state.update { it.copy(errorMessage = "ID de oferta inválido") }
        }
    }

    private fun observeOffer(uuid: String) {
        viewModelScope.launch {
            jobOfferUseCases.observeJobOffers().collect { offers ->
                val offer = offers.find { it.uuid == uuid || it.id.toString() == uuid }
                if (offer != null) {
                    val wasNull = (jobOfferId == null)
                    jobOfferId = offer.id
                    _state.update { it.copy(jobOffer = offer) }
                    if (wasNull) {
                        observeWorkers()
                        updateWorkers()
                    }
                }
            }
        }
    }

    fun onEvent(event: JobOfferTrackingEvent) {
        when (event) {
            is JobOfferTrackingEvent.Refresh -> {
                checkExistingContracts()
                val identifier = jobOfferUuid ?: jobOfferId?.toString()
                if (identifier != null) {
                    fetchApplications(identifier)
                }
                if (jobOfferUuid != null) {
                    loadOfferAndWorkers(jobOfferUuid)
                } else if (jobOfferId != null) {
                    updateWorkers()
                }
            }
            is JobOfferTrackingEvent.ClearError -> _state.update { it.copy(errorMessage = "") }
            is JobOfferTrackingEvent.SelectApplication -> selectApplication(event.application)
            is JobOfferTrackingEvent.DismissApplicationDetail -> {
                _state.update { it.copy(selectedApplication = null, workerReviews = emptyList()) }
            }
        }
    }

    private fun selectApplication(application: JobApplication?) {
        _state.update {
            it.copy(
                selectedApplication = application,
                workerReviews = emptyList(),
                isLoadingReviews = application?.workerUserId != null
            )
        }
        val workerUserId = application?.workerUserId
        if (workerUserId != null) {
            viewModelScope.launch {
                reviewUseCases.getReviewsForUser(workerUserId).collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _state.update {
                                it.copy(
                                    workerReviews = response.data.content,
                                    isLoadingReviews = false
                                )
                            }
                        }
                        is ApiResponse.Error -> {
                            _state.update { it.copy(isLoadingReviews = false) }
                        }
                        is ApiResponse.Failure -> {
                            _state.update { it.copy(isLoadingReviews = false) }
                        }
                        ApiResponse.Loading -> {
                            _state.update { it.copy(isLoadingReviews = true) }
                        }
                    }
                }
            }
        }
    }

    private fun loadOfferAndWorkers(uuid: String) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, errorMessage = "") }
            val localOffer = jobOfferUseCases.getJobOfferByUuid(uuid)
            if (localOffer != null) {
                jobOfferId = localOffer.id
                _state.update { it.copy(jobOffer = localOffer, isRefreshing = false) }
                observeWorkers()
                updateWorkers()
            }
            jobOfferUseCases.updateJobOffers(fetchFromRemote = true).collect { syncResponse ->
                when (syncResponse) {
                    is ApiResponse.Success -> {
                        val refreshedOffer = jobOfferUseCases.getJobOfferByUuid(uuid)
                        if (refreshedOffer != null) {
                            jobOfferId = refreshedOffer.id
                            _state.update { it.copy(jobOffer = refreshedOffer, isRefreshing = false) }
                            observeWorkers()
                            updateWorkers()
                        } else {
                            _state.update { it.copy(isRefreshing = false) }
                        }
                    }
                    is ApiResponse.Error -> {
                        _state.update {
                            it.copy(isRefreshing = false, errorMessage = if (localOffer == null) syncResponse.errorMessage else it.errorMessage)
                        }
                    }
                    is ApiResponse.Failure -> {
                        _state.update {
                            it.copy(isRefreshing = false, errorMessage = if (localOffer == null) syncResponse.errorMessage else it.errorMessage)
                        }
                    }
                    ApiResponse.Loading -> {
                        if (localOffer == null) {
                            _state.update { it.copy(isRefreshing = true) }
                        }
                    }
                }
            }
        }
    }

    private fun startApplicationsPolling(identifier: String) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (isActive) {
                fetchApplications(identifier)
                delay(5000)
            }
        }
    }

    private fun checkExistingContracts() {
        viewModelScope.launch {
            contractUseCases.getMyContracts()
                .catch { /* ignore */ }
                .collect { response ->
                    if (response is ApiResponse.Success) {
                        val contracts = response.data
                        val offerId = jobOfferId
                        val appIds = _state.value.applications.mapNotNull { it.jobApplicationId.toLongOrNull() }
                        val matching = contracts.find { contract ->
                            (offerId != null && contract.jobOfferId == offerId) ||
                            (contract.jobApplicationId in appIds)
                        }
                        if (matching != null) {
                            _state.update {
                                it.copy(
                                    hasExistingContract = true,
                                    existingContractId = matching.id
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun fetchApplications(identifier: String) {
        viewModelScope.launch {
            jobApplicationUseCases.getJobApplicationsByOffer(identifier).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _state.update {
                            it.copy(
                                applications = response.data,
                                isRefreshing = false
                            )
                        }
                        checkExistingContracts()
                    }
                    is ApiResponse.Error -> {
                        _state.update { it.copy(isRefreshing = false) }
                    }
                    is ApiResponse.Failure -> {
                        _state.update { it.copy(isRefreshing = false) }
                    }
                    ApiResponse.Loading -> {
                        // Do not show full-screen spinner on background polling
                    }
                }
            }
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

    override fun onCleared() {
        super.onCleared()
        pollingJob?.cancel()
    }

    companion object {
        private const val TAG = "JobOfferTrackingViewModel"
    }
}