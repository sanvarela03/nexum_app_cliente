package com.example.nexum_cliente.utils.location

import com.google.android.gms.maps.model.LatLng

interface LocationClient {
    interface LocationCallback {
        fun onLocationResult(latLng: LatLng, address: String)
        fun onError(message: String)
    }

    fun getCurrentLocation(callback: LocationCallback)
}
