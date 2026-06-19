package com.example.nexum_cliente.domain.use_cases.common

import android.net.Uri
import android.util.Log
import com.example.nexum_cliente.data.local_storage.AsyncStorage
import com.example.nexum_cliente.common.ENTRY_SEPARATOR
import com.example.nexum_cliente.common.URI_URL_SEPARATOR
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

    /**
     * Sube una imagen a Firebase sin persistirla en AsyncStorage.
     * Útil para listas de imágenes (ej. evidencias de JobOffer) donde
     * no se necesita guardar cada URL individualmente en local.
     */
    suspend fun uploadImageOnly(
        uri: Uri,
        folder: String = "job_offer_images"
    ): Result<String> {
        return try {
            val url = uploadImageToFirebase(uri, folder)
                ?: return Result.failure(Exception("Upload failed"))
            Result.success(url)
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

    /**
     * Elimina una imagen de Firebase sin tocar AsyncStorage.
     * Usar para listas de imágenes (ej. evidencias de JobOffer).
     */
    suspend fun deleteImageOnly(url: String): Result<Unit> {
        return try {
            if (url.isNotEmpty()) {
                deleteImageFromFirebase(url)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("MediaManagementUseCase", "Error deleting image", e)
            Result.failure(e)
        }
    }

    // ─── Draft de imágenes para JobOffer ─────────────────────────────────────
    // Las URLs se serializan como texto separado por '|' ya que Firebase URLs
    // nunca contienen ese carácter, evitando así dependencias de JSON.

    /** Recupera las URLs del draft guardadas en DataStore. */
    suspend fun getDraftImageUrls(draftKey: String): List<String> {
        val raw = asyncStorage.getItem(draftKey) ?: return emptyList()
        return raw.split("|").filter { it.isNotBlank() }
    }

    /** Añade una URL al final del draft persistido. */
    suspend fun addDraftImageUrl(draftKey: String, url: String) {
        val current = getDraftImageUrls(draftKey).toMutableList()
        if (!current.contains(url)) {
            current.add(url)
            asyncStorage.setItem(draftKey, current.joinToString("|"))
        }
    }

    /** Elimina una URL específica del draft persistido. */
    suspend fun removeDraftImageUrl(draftKey: String, url: String) {
        val updated = getDraftImageUrls(draftKey).filter { it != url }
        asyncStorage.setItem(draftKey, updated.joinToString("|"))
    }

    /** Limpia completamente el draft (al publicar o cancelar). */
    suspend fun clearDraftImageUrls(draftKey: String) {
        asyncStorage.removeItem(draftKey)
    }

    // ─── Cache persistente URI → Firebase URL ────────────────────────────────
    // Solo se guarda al publicar una oferta de forma exitosa.
    // Permite reutilizar URLs ya subidas si el usuario selecciona la misma foto.

    /**
     * Retorna el cache completo como Map<URI_string, Firebase_URL>.
     */
    suspend fun getUriUrlCache(cacheKey: String): Map<String, String> {
        val raw = asyncStorage.getItem(cacheKey) ?: return emptyMap()
        return raw.split(ENTRY_SEPARATOR)
            .filter { it.contains(URI_URL_SEPARATOR) }
            .associate { entry ->
                val parts = entry.split(URI_URL_SEPARATOR, limit = 2)
                parts[0] to parts[1]
            }
    }

    /**
     * Busca la URL de Firebase para una URI concreta.
     * @return null si la URI nunca ha sido subida en un submit exitoso.
     */
    suspend fun getCachedUrl(cacheKey: String, uri: String): String? {
        return getUriUrlCache(cacheKey)[uri]
    }

    /**
     * Persiste un conjunto de pares URI→URL al DataStore.
     * Debe llamarse SOLO tras un submit exitoso para no acumular URLs huérfanas.
     */
    suspend fun saveUriUrlsToCache(cacheKey: String, uriToUrlMap: Map<String, String>) {
        val existing = getUriUrlCache(cacheKey).toMutableMap()
        existing.putAll(uriToUrlMap) // Merge: preserva entradas anteriores
        val serialized = existing.entries.joinToString(ENTRY_SEPARATOR) {
            "${it.key}$URI_URL_SEPARATOR${it.value}"
        }
        asyncStorage.setItem(cacheKey, serialized)
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
