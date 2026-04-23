package com.example.nexum_cliente.ui.presenter.map

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import timber.log.Timber

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
    initialLocation: LatLng? = null,
    onMapClick: (LatLng, String) -> Unit,
) {
    val context = LocalContext.current
    val defaultLocation = LatLng(4.6097, -74.0817) // Bogotá as default instead of London

    val userLocation = viewModel.userLocation.value
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    AskLocationPermission(viewModel, context, fusedLocationClient)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation ?: defaultLocation, 15f)
    }

    val markerState = rememberMarkerState(position = initialLocation ?: defaultLocation)

    LaunchedEffect(userLocation) {
        // Si no hay una ubicación inicial y obtenemos la del usuario, mover ahí
        if (initialLocation == null && userLocation != null) {
            markerState.position = userLocation
            cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation, 15f)
            viewModel.getAddressFromLatLng(
                context = context,
                lat = userLocation.latitude,
                lng = userLocation.longitude
            ) { address ->
                onMapClick(userLocation, address ?: "")
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = true
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = true,
            zoomControlsEnabled = true
        ),
        onMapClick = { latLng ->
            markerState.position = latLng
            viewModel.getAddressFromLatLng(
                context = context,
                lat = latLng.latitude,
                lng = latLng.longitude
            ) { address ->
                onMapClick(latLng, address ?: "")
            }
        }
    ) {
        Marker(
            state = markerState,
            title = "Ubicación seleccionada",
            snippet = "Esta será la ubicación de la oferta."
        )
    }
}

@Composable
private fun AskLocationPermission(
    viewModel: MapViewModel,
    context: Context,
    fusedLocationClient: FusedLocationProviderClient
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Fetch the user's location and update the camera if permission is granted
            viewModel.fetchUserLocation(context, fusedLocationClient)
        } else {
            // Handle the case when permission is denied
            Timber.e("Location permission was denied by the user.")
        }
    }
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            // Check if the location permission is already granted
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // Fetch the user's location and update the camera
                viewModel.fetchUserLocation(context, fusedLocationClient)
            }

            else -> {
                // Request the location permission if it has not been granted
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}