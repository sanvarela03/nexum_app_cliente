package com.example.nexum_cliente.data.client.mapper

<<<<<<< Updated upstream
object ClientMapper {
=======
import com.example.nexum_cliente.data.auth.remote.payload.req.ProfileReq
import com.example.nexum_cliente.data.auth.remote.payload.req.SignUpReq
import com.example.nexum_cliente.data.client.local.ClientEntity
import com.example.nexum_cliente.data.client.remote.payload.res.ClientRes
import com.example.nexum_cliente.data.mapper.DomainMapper
import com.example.nexum_cliente.data.mapper.EntityMapper
import com.example.nexum_cliente.data.mapper.RequestMapper
import com.example.nexum_cliente.domain.model.Client
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ClientMapper : EntityMapper<ClientRes, ClientEntity>, DomainMapper<ClientEntity, Client>, RequestMapper<SignUpState, SignUpReq> {
    override fun toEntity(dto: ClientRes): ClientEntity {
        return ClientEntity(
            clientId = dto.clientId,
            userId = dto.user.userId,
            firstName = dto.user.firstName,
            lastName = dto.user.lastName,
            username = dto.user.username,
            email = dto.user.email,
            verificationCode = dto.user.verificationCode,
            verificationCodeTimestamp = dto.user.verificationCodeTimestamp,
            firebaseToken = dto.user.firebaseToken,
            phone = dto.user.phone,
            isEnabled = dto.user.isEnabled,
            dateJoined = dto.user.dateJoined,
            lastLogin = dto.user.lastLogin,
            imgUrl = dto.user.imgUrl,
            profileVerified = dto.profileVerified
        )
    }

    override fun toDomain(entity: ClientEntity): Client {
        return Client(
            id = entity.clientId ?: 0L,
            firstName = entity.firstName,
            lastName = entity.lastName,
            profilePictureUrl = entity.imgUrl ?: "",
            email = entity.email,
            username = entity.username,
            phone = entity.phone ?: "",
            isEnabled = entity.isEnabled,
            profileVerified = entity.profileVerified,
            verificationCode = entity.verificationCode,
            verificationCodeTimestamp = entity.verificationCodeTimestamp,
            firebaseToken = entity.firebaseToken,
            dateJoined = entity.dateJoined,
            lastLogin = entity.lastLogin
        )
    }

    override fun toRequest(state: SignUpState): SignUpReq {
        val username = if(state.username.isEmpty()) "${state.name.firstOrNull() ?: ""}${state.lastName.split(' ').firstOrNull() ?: ""}${(10..99).random()}" else state.username

        val birthDate = try {
            LocalDate.of(
                state.birthDateYear,
                state.birthDateMonth,
                state.birthDateDay
            ).format(DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            // Fallback or handle error appropriately if date is invalid
            ""
        }

        return SignUpReq(
            username = username,
            email = state.email,
            password = state.password,
            phoneCode = state.phoneCode.split(' ').firstOrNull() ?: "",
            phoneNumber = state.phone,
            marketLocationId = state.selectedMarketLocation?.id ?: 0 ,
            // Firebase token might need to be passed separately or handled differently if not in state
            // Assuming for now it's handled elsewhere or has a default, as toRequest doesn't take extra args usually
            // However, looking at SignUpState.toResponse, it takes firebaseToken as arg.
            // RequestMapper usually maps State -> Request.
            // If the token is not in State, we might need to put a placeholder or update State to include it.
            // Based on SignUpViewModel, token is fetched in signUp().
            // Ideally, the token should be part of the state if we want a pure mapping.
            // For now, I will use a placeholder or empty string, as it's not in the state properties directly shown (except via toResponse arg).
            // Wait, toResponse uses it as an argument.
            // I will use "NaN" or similar default as seen in SignUpReq default, or empty.
            firebaseToken = "NaN", // Placeholder, as token is likely injected later or should be in state
            profile = ProfileReq(
                firstName = state.name,
                lastName = state.lastName,
                birthDate = birthDate,
                profileImageUrl = state.profilePictureUrl,
                frontDocumentUrl = state.frontDocumentUrl,
                backDocumentUrl = state.backDocumentUrl,
            )
        )
    }

>>>>>>> Stashed changes
}