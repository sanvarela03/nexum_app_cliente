package com.example.nexum_cliente.ui.components

import androidx.compose.ui.unit.Dp
import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 02/09/2025
 * @version 1.0
 */
@Composable
fun AvatarPicturePicker(
    imageUri: Uri?,                           // pass current image (or null)
    onImageSelected: (Uri) -> Unit,           // called with new image Uri
    modifier: Modifier = Modifier,
    size: Dp = 96.dp
) {
    // System photo picker (no runtime permission needed)

}
