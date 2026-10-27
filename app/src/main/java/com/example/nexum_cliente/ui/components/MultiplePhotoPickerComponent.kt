package com.example.nexum_cliente.ui.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.nexum_cliente.common.MAX_JOB_OFFER_IMAGES

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalLayoutApi::class)
@Composable
fun MultiplePhotoPickerComponent(
    selectedImages: List<Uri> = emptyList(),
    maxImages: Int = MAX_JOB_OFFER_IMAGES,
    onAddImage: (List<Uri>) -> Unit,
    onRemoveImage: (Int) -> Unit,
    onLimitExceeded: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val remainingSlots = (maxImages - selectedImages.size).coerceAtLeast(0)
    val isLimitReached = selectedImages.size >= maxImages

    // Launcher para selección múltiple (cuando quedan 2 o más espacios)
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = if (remainingSlots > 1) remainingSlots else 2
        ),
        onResult = { uris ->
            if (!uris.isNullOrEmpty()) {
                val toAdd = uris.take(remainingSlots)
                if (uris.size > remainingSlots) {
                    onLimitExceeded?.invoke()
                }
                onAddImage(toAdd)
            }
        }
    )

    // Launcher para selección única (cuando queda exactamente 1 espacio)
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                if (remainingSlots > 0) {
                    onAddImage(listOf(it))
                } else {
                    onLimitExceeded?.invoke()
                }
            }
        }
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Botón para agregar imagen (se oculta cuando se alcanza el límite máximo)
            if (!isLimitReached) {
                OutlinedIconButton(
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = IconButtonColors(
                        contentColor = Color(0xFF009963),
                        containerColor = Color(0xFFF9FBF9),
                        disabledContainerColor = Color(0xFFF5F5F5),
                        disabledContentColor = Color(0xFFBDBDBD),
                    ),
                    border = BorderStroke(1.5.dp, Color(0xFF009963).copy(alpha = 0.5f)),
                    onClick = {
                        if (remainingSlots > 1) {
                            multiplePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        } else if (remainingSlots == 1) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        } else {
                            onLimitExceeded?.invoke()
                        }
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar foto",
                            tint = Color(0xFF009963),
                            modifier = Modifier.size(32.dp),
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${selectedImages.size}/$maxImages",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF009963)
                        )
                    }
                }
            }

            if (selectedImages.isNotEmpty()) {
                selectedImages.forEachIndexed { index, uri ->
                    ImagePreview(
                        uri = uri,
                        onRemoveImage = onRemoveImage,
                        index = index
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun ImagePreview(
    uri: Any?,
    onRemoveImage: (Int) -> Unit,
    index: Int,
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .fillMaxSize()
) {
    Box(modifier = Modifier.size(100.dp)) {
        if (uri != null && uri.toString().isNotEmpty()) {
            GlideImage(
                model = uri,
                contentDescription = "Selected Image",
                modifier = modifier,
                contentScale = contentScale
            )

            IconButton(
                onClick = { onRemoveImage(index) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .clip(RoundedCornerShape(50))
                    .size(24.dp)
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun ImagePreview(
    uri: Any?,
    onRemoveImage: (Int) -> Unit,
    index: Int,
    contentScale: ContentScale = ContentScale.Crop,
    containerModifier: Modifier = Modifier
        .size(100.dp),
    contentModifier: Modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .fillMaxSize()
) {
    Box(
        modifier = containerModifier,
//        contentAlignment = Alignment.Center
    ) {
        // Show image only if we actually have one
        if (uri != null && uri != "") {
            GlideImage(
                model = uri,
                contentDescription = "Selected Image",
                modifier = contentModifier,
                contentScale = contentScale
            )

            // Show remove icon only when an image is shown
            IconButton(
                onClick = { onRemoveImage(index) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .clip(RoundedCornerShape(50))
                    .size(24.dp)
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
