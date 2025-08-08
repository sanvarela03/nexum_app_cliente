package com.example.neuxum_cliente.ui.presenter.map

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
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
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
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val london = LatLng(51.5074, -0.1278)

    val userLocation = viewModel.userLocation.value
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    AskLocationPermission(viewModel, context, fusedLocationClient)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(london, 10f)
    }



    val markerState = rememberMarkerState(position = london)

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        // If the user's location is available, place a marker on the map
        userLocation?.let {
            Marker(
                state = MarkerState(position = it), // Place the marker at the user's location
                title = "Tu estas aquí 😎✌🏻", // Set the title for the marker
                snippet = "Aquí es donde te encuentras actualmente." // Set the snippet for the marker
            )
            // Move the camera to the user's location with a zoom level of 10f
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 10f)
        }
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