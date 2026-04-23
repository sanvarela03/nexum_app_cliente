package com.example.nexum_cliente.utils.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class FusedLocationClientImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationClient {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(callback: LocationClient.LocationCallback) {
        // Usamos getCurrentLocation en lugar de lastLocation para asegurar una ubicación fresca
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    val geocoder = Geocoder(context, Locale.getDefault())

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        // Versión moderna (API 33+) no bloqueante
                        geocoder.getFromLocation(location.latitude, location.longitude, 1, object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) {
                                val addressName = if (addresses.isNotEmpty()) {
                                    addresses[0].getAddressLine(0)
                                } else {
                                    "${location.latitude}, ${location.longitude}"
                                }
                                callback.onLocationResult(latLng, addressName)
                            }

                            override fun onError(errorMessage: String?) {
                                callback.onLocationResult(latLng, "${location.latitude}, ${location.longitude}")
                            }
                        })
                    } else {
                        // Versión antigua (DEPRECATED) bloqueante, se corre en hilo secundario
                        Thread {
                            try {
                                @Suppress("DEPRECATION")
                                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                val addressName = if (!addresses.isNullOrEmpty()) {
                                    addresses[0].getAddressLine(0)
                                } else {
                                    "${location.latitude}, ${location.longitude}"
                                }
                                callback.onLocationResult(latLng, addressName)
                            } catch (e: Exception) {
                                callback.onLocationResult(latLng, "${location.latitude}, ${location.longitude}")
                            }
                        }.start()
                    }
                } else {
                    callback.onError("No se pudo obtener la ubicación actual. Asegúrate de tener el GPS activo.")
                }
            }
            .addOnFailureListener { e ->
                callback.onError(e.message ?: "Error al obtener la ubicación")
            }
    }
}
