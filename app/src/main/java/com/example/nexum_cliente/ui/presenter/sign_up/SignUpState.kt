package com.example.nexum_cliente.ui.presenter.sign_up

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.nexum_cliente.data.auth.remote.payload.req.ProfileReq
import com.example.nexum_cliente.data.auth.remote.payload.req.SignUpReq
import com.example.nexum_cliente.data.country.local.CountryEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 05/08/2025
 * @version 1.0
 */
data class SignUpState(
    var email: String = "",
    var username: String = "",
    var name: String = "",
    var lastName: String = "",
    var countryFlagEmoji: String = "",
    var phoneCode: String = "",
    var phone: String = "",
    var birthDate: String = "",
    var birthDateDay: Int = 8,
    var birthDateMonth: Int = 6,
    var birthDateYear: Int = 1978,
    var city: CityState = CityState(),
    var documentNumber: String = "",
    var frontDocumentUri: Uri = Uri.EMPTY,
    var backDocumentUri: Uri = Uri.EMPTY,
    var frontDocumentUrl: String = "",
    var backDocumentUrl: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var profilePictureUri: Uri = Uri.EMPTY,
    var profilePictureUrl: String = "",
    var cities: List<CityState> = emptyList(),
    var countryCode: String = "",
    var isLoadingCountries: Boolean = false,
    var countriesError: String? = "",
    var phoneRegex: String = "",
    var countries: List<CountryEntity> = emptyList(),
    var countryCodes: List<String> = listOf(""),
    var countriesByCountryCode: Map<String, String> = mapOf(
        "🇨🇴 +57" to "CO",
        "🇲🇽 +52" to "MX",
        "🇨🇱 +56" to "CL"
    ),
    var emailError: Boolean = false,
    var nameError: Boolean = false,
    var lastNameError: Boolean = false,
    var phoneCodeError: Boolean = false,
    var phoneError: Boolean = false,
    var birthDateError: Boolean = false,
    var cityError: Boolean = false,
    var documentNumberError: Boolean = false,
    var frontDocumentUriError: Boolean = false,
    var backDocumentUriError: Boolean = false,
    var frontDocumentUrlError: Boolean = false,
    var backDocumentUrlError: Boolean = false,
    var passwordError: Boolean = false,
    var confirmPasswordError: Boolean = false,
    var profilePictureUriError: Boolean = false,
    var profilePictureUrlError: Boolean = false,
    var errorMessage: String = "",
    var isRefreshing: Boolean = false,
    var isSignedUp: Boolean = false,
    var signUpError: Boolean = false,
    var signUpResponse: String = "",
    ){
    @RequiresApi(Build.VERSION_CODES.O)
    fun toResponse(firebaseToken: String) : SignUpReq {
        username = "${name[0]}${lastName.split(' ')[0]}${(10..99).random()}"
        return SignUpReq(
            username = username,
            email = email,
            password = password,
            phoneCode = phoneCode.split(' ')[0],
            phoneNumber = phone,
            marketLocationId = city.id,
            firebaseToken = firebaseToken,
            profile = ProfileReq(
                firstName = name,
                lastName = lastName,
                birthDate = LocalDate.of(
                    birthDateYear.toInt(),
                    birthDateMonth.toInt(),
                    birthDateDay.toInt()
                ).format(DateTimeFormatter.ISO_LOCAL_DATE),
                profileImageUrl = profilePictureUrl,
                frontDocumentUrl = frontDocumentUrl,
                backDocumentUrl = backDocumentUrl,
            )
        )
    }
}


data class CityState(
    var id: Long = 0,
    var city: String = "",
    var state: String = "",
    var country: String = "",
    var countryCode: String = "",
    var flagEmoji: String = ""
)
