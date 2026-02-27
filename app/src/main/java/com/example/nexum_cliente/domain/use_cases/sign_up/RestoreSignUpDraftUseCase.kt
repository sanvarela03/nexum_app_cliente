package com.example.nexum_cliente.domain.use_cases.sign_up

import com.example.nexum_cliente.data.local_storage.AsyncStorage
import javax.inject.Inject

data class SignUpDraft(
    val frontDocumentUrl: String,
    val backDocumentUrl: String,
    val profilePictureUrl: String
)

class RestoreSignUpDraftUseCase @Inject constructor(
    private val asyncStorage: AsyncStorage
) {
    suspend operator fun invoke(): SignUpDraft {
        val frontUrl = asyncStorage.getItem("frontDocumentUrl") ?: ""
        val backUrl = asyncStorage.getItem("backDocumentUrl") ?: ""
        val profileUrl = asyncStorage.getItem("profilePictureUrl") ?: ""
        return SignUpDraft(
            frontDocumentUrl = frontUrl,
            backDocumentUrl = backUrl,
            profilePictureUrl = profileUrl
        )
    }
}
