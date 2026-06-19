package com.example.nexum_cliente.common

import android.net.Uri
import android.util.Log
import com.example.nexum_cliente.domain.use_cases.common.MediaManagementUseCase
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaUploadHandler @Inject constructor(
    private val mediaManagementUseCase: MediaManagementUseCase
) {
    companion object {
        private const val TAG = "MediaUploadHandler"
    }

    fun handleMediaChange(
        scope: CoroutineScope,
        newUri: Uri?,
        currentUrl: String,
        storageKey: String,
        onSuccess: (String) -> Unit,
        onDelete: () -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        Log.d(TAG, "handleMediaChange: $newUri ${newUri?.toString()?.length}")
        
        scope.launch {
            if (newUri == null || newUri == Uri.EMPTY) {
                mediaManagementUseCase.deleteImage(currentUrl, storageKey)
                    .onSuccess { onDelete() }
                    .onFailure { exception ->
                        Log.e(TAG, "Error deleting image", exception)
                        val isNotFound = exception is StorageException && 
                                exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND
                        
                        if (isNotFound) {
                            Log.w(TAG, "Image to delete was not found in Firebase 🤔. Deleting locally ♻️", exception)
                            onDelete()
                        } else {
                            Log.e(TAG, "❌ Error deleting image", exception)
                            onError(exception)
                        }
                    }
            } else {
                mediaManagementUseCase.uploadAndSaveImage(newUri, currentUrl, storageKey)
                    .onSuccess(onSuccess)
                    .onFailure { 
                        Log.e(TAG, "Error uploading image ⏫", it) 
                        onError(it)
                    }
            }
        }
    }

    /**
     * Sube una imagen a Firebase directamente (sin AsyncStorage).
     * Usar para listas de imágenes como las evidencias de un JobOffer.
     *
     * @param folder Carpeta destino en Firebase Storage (default: "job_offer_images")
     */
    fun handleImageUpload(
        scope: CoroutineScope,
        uri: Uri,
        folder: String = "job_offer_images",
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        Log.d(TAG, "handleImageUpload: $uri -> folder: $folder")
        scope.launch {
            mediaManagementUseCase.uploadImageOnly(uri, folder)
                .onSuccess(onSuccess)
                .onFailure {
                    Log.e(TAG, "❌ Error uploading image to $folder", it)
                    onError(it)
                }
        }
    }

    /**
     * Elimina una imagen de Firebase sin tocar AsyncStorage.
     * Usar para quitar imágenes de una lista (ej. JobOffer).
     */
    fun handleImageDelete(
        scope: CoroutineScope,
        url: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        Log.d(TAG, "handleImageDelete: $url")
        scope.launch {
            mediaManagementUseCase.deleteImageOnly(url)
                .onSuccess { onSuccess() }
                .onFailure { exception ->
                    Log.e(TAG, "Error deleting image", exception)
                    val isNotFound = exception is StorageException &&
                            exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND
                    if (isNotFound) {
                        Log.w(TAG, "Image not found in Firebase, removing locally ♻️")
                        onSuccess() // Tratar como éxito: si no existía, el resultado es el mismo
                    } else {
                        onError(exception)
                    }
                }
        }
    }
}
