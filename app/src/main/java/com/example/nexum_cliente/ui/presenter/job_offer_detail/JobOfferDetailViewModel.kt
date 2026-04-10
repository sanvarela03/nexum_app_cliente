package com.example.nexum_cliente.ui.presenter.job_offer_detail

import androidx.lifecycle.ViewModel
import com.example.nexum_cliente.domain.use_cases.job_offer.JobOfferUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 4/5/2026
 * @version 1.0
 */
@HiltViewModel
class JobOfferDetailViewModel @Inject constructor(
    private val jobOfferUseCases: JobOfferUseCases
) : ViewModel() {

}