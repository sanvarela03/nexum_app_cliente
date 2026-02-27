package com.example.nexum_cliente.domain.use_cases.common

import android.net.Uri
import android.util.Log
import com.example.nexum_cliente.data.local_storage.AsyncStorage
import com.example.nexum_cliente.service.FirebaseStorageService
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Caso de uso para manejar la lógica de subida, reemplazo y eliminación de imágenes.
 * Convierte los callbacks de Firebase a Coroutines para un código más limpio.
 */
@Singleton
class MediaManagementUseCase @Inject constructor(
    private val asyncStorage: AsyncStorage
) {

    suspend fun uploadAndSaveImage(
        uri: Uri,
        oldUrl: String,
        storageKey: String,
        folder: String = "documents"
    ): Result<String> {
        return try {
            // 1. Subir la nueva imagen
            val newUrl = uploadImageToFirebase(uri, folder)
                ?: return Result.failure(Exception("Upload failed"))

            // 2. Guardar en almacenamiento local
            asyncStorage.setItem(storageKey, newUrl)

            // 3. Borrar la imagen antigua si existe y es diferente
            if (oldUrl.isNotEmpty() && oldUrl != newUrl) {
                deleteImageFromFirebase(oldUrl)
            }

            Result.success(newUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteImage(currentUrl: String, storageKey: String): Result<Unit> {
        return try {
            if (currentUrl.isNotEmpty()) {
                deleteImageFromFirebase(currentUrl)
                asyncStorage.setItem(storageKey, "")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("MediaManagementUseCase", "Error deleting image", e)
            Result.failure(e)
        }
    }

    // Wrapper para convertir Callback a Suspend Function
    private suspend fun uploadImageToFirebase(uri: Uri, folder: String): String? =
        suspendCancellableCoroutine { continuation ->
            FirebaseStorageService.uploadImage(
                imageUri = uri,
                username = "temp_user_${System.currentTimeMillis()}", // Mejorar lógica de nombre si es necesario
                folder = folder
            ) { url ->
                if (continuation.isActive) {
                    continuation.resume(url)
                }
            }
        }

    // Wrapper para convertir Callback a Suspend Function
    private suspend fun deleteImageFromFirebase(url: String): Unit =
        suspendCancellableCoroutine { continuation ->
            FirebaseStorageService.deleteImage(url) { success, exception ->
                if (continuation.isActive) {
                    if (success) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(exception ?: Exception("Unknown error deleting image"))
                    }
                }
            }
        }
}
