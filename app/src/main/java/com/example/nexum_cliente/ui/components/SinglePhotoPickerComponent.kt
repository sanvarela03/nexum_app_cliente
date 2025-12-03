package com.example.nexum_cliente.ui.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 20/08/2025
 * @version 1.0
 */
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalLayoutApi::class)
@Composable
fun SinglePhotoPickerComponent(
    selectedImages: MutableList<Uri> = mutableStateListOf(),
    onAddImage: (Uri) -> Unit,
    onRemoveImage: (Int) -> Unit,
    onClick: () -> Unit = {}
) {
    Log.d("PhotoPickerComponent", "selectedImages: $selectedImages")
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                onAddImage(it)
            }
        }
    )

}