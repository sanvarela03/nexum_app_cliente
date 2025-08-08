package com.example.neuxum_cliente.ui.presenter.job_offer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
@HiltViewModel
class JobOfferViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(JobOfferState())

    var showMapDialog by mutableStateOf(false)
}