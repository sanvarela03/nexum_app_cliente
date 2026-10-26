package com.example.nexum_cliente.ui.presenter.job_applications

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.domain.use_cases.job_application.JobApplicationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 9/6/2026
 * @version 1.0
 */
@HiltViewModel
class JobApplicationsViewModel @Inject constructor(
    private val useCases: JobApplicationUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val jobOfferId: String? = savedStateHandle.get<String>("jobOfferId")

    private val _state = MutableStateFlow(JobApplicationsState())
    val state = _state.asStateFlow()

    init {
        if (jobOfferId != null) {
            observeJobApplications(jobOfferId)
            fetchJobApplicationsFromRemote(jobOfferId)
        } else {
            _state.update { it.copy(error = "ID de oferta inválido") }
        }
    }

    private fun fetchJobApplicationsFromRemote(jobOfferId: String) {
        viewModelScope.launch {
            useCases.getJobApplicationsByOffer(jobOfferId).collect { response ->
                when (response) {
                    is com.example.nexum_cliente.data.global_payload.res.ApiResponse.Success -> {
                        if (response.data.isNotEmpty()) {
                            _state.update { it.copy(jobApplications = response.data, isLoading = false) }
                        }
                    }
                    is com.example.nexum_cliente.data.global_payload.res.ApiResponse.Error -> {
                        _state.update { it.copy(isLoading = false) }
                    }
                    is com.example.nexum_cliente.data.global_payload.res.ApiResponse.Failure -> {
                        _state.update { it.copy(isLoading = false) }
                    }
                    com.example.nexum_cliente.data.global_payload.res.ApiResponse.Loading -> {
                        if (_state.value.jobApplications.isEmpty()) {
                            _state.update { it.copy(isLoading = true) }
                        }
                    }
                }
            }
        }
    }

    private fun observeJobApplications(jobOfferId: String) {
        viewModelScope.launch {
            useCases.observeJobApplications(jobOfferId)
                .catch { e ->
                    Log.e(TAG, "observeJobApplications: Error al obtener las solicitudes", e)
                    _state.update { it.copy(isLoading = false) }
                    _state.update { it.copy(error = e.message ?: "Error desconocido") }
                }
                .collect { jobApplications ->
                    if (jobApplications.isNotEmpty()) {
                        _state.update { it.copy(jobApplications = jobApplications, isLoading = false) }
                    }
                }
        }
    }

    companion object {
        private const val TAG = "JobApplicationsViewModel"
    }
}