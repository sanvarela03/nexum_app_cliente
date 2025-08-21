package com.example.neuxum_cliente.ui.presenter.job_offer

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
data class JobOfferState(
    var title: String = "",
    var description: String = "",
    var address: String = "",
    var categoryId: Long = 0,
    var requestedDate: String = "",
    var images: MutableList<Uri> = mutableStateListOf(),
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var showMapDialog: Boolean = false
)