package com.example.nexum_cliente.ui.presenter.sign_up

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexum_cliente.data.global_payload.res.ApiResponse
import com.example.nexum_cliente.data.market_location.local.MarketLocationDao
import com.example.nexum_cliente.data.market_location.local.MarketLocationEntity
import com.example.nexum_cliente.domain.use_cases.auth.AuthUseCases
import com.example.nexum_cliente.domain.use_cases.country.CountryUseCases
import com.example.nexum_cliente.domain.use_cases.market_location.MarketLocationUseCases
import com.example.nexum_cliente.service.FirebaseStorageService
import com.example.nexum_cliente.user_preferences.UserPreferences
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import java.net.SocketTimeoutException
import java.net.UnknownHostException

// Data class to hold the UI state for market location selection
data class MarketLocationUiState(
    val allLocations: List<MarketLocationEntity> = emptyList(),
    val countries: List<String> = emptyList(),
    val states: List<String> = emptyList(),
    val cities: List<MarketLocationEntity> = emptyList(), // Or List<String> if you only need city names
    val selectedCountry: String? = null,
    val selectedState: String? = null,
    val selectedCityId: Long? = null, // Or selectedCityName: String?
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SignUpViewModel
@Inject constructor(
    private val authUseCases: AuthUseCases,
    private val marketLocationUseCases: MarketLocationUseCases,
    private val countryUseCases: CountryUseCases,
    private val userPreferences: UserPreferences,
    private val dao: MarketLocationDao,
//    private val viewModelScope: CoroutineScope
) : ViewModel() {
    private var signUpJob : Job? = null
    private val _isLoading = MutableStateFlow(false)
    private val _selectedCountry = MutableStateFlow<String?>(null)
    private val _selectedState = MutableStateFlow<String?>(null)
    private val _selectedCityId = MutableStateFlow<Long?>(null) // Or String for name

    var state by mutableStateOf(SignUpState())
    var signUpEmailValidationPassed by mutableStateOf(false)
    var signUpUserDataValidationPassed by mutableStateOf(false)
    var signUpPhoneDataValidationPassed by mutableStateOf(false)
    var signUpBirthDateValidationPassed by mutableStateOf(false)
    var signUpCityValidationPassed by mutableStateOf(false)
    var signUpDocumentNumberValidationPassed by mutableStateOf(false)
    var signUpDocumentImagesValidationPassed by mutableStateOf(false)
    var signUpPasswordValidationPassed by mutableStateOf(false)
    var signUpProfilePictureValidationPassed by mutableStateOf(false)

    private var updateMarketLocations: Job? = null

    init {
        viewModelScope.launch {
            val locations = dao.getAllLocations()
            Log.d("SignUpViewModel3", "getAllLocations: $locations")
            val backFlow = userPreferences.getBackDocumentUrl().distinctUntilChanged()
            val frontFlow = userPreferences.getFrontDocumentUrl().distinctUntilChanged()
            val profileFlow = userPreferences.getProfilePictureUrl().distinctUntilChanged()

            combine(backFlow, frontFlow, profileFlow) { back, front, profile ->
                Triple(back, front, profile)
            }
                .distinctUntilChanged()
                .collectLatest { (back, front, profile) ->
                    state = state.copy(
                        backDocumentUrl = back ?: state.backDocumentUrl,
                        frontDocumentUrl = front ?: state.frontDocumentUrl,
                        profilePictureUrl = profile ?: state.profilePictureUrl
                    )
                    validateDocumentImages()
                    validateProfilePictureImage()
                }
        }
        Log.d("SignUpViewModel1", "init 1")
        updateMarketLocations(true)
        observeAllLocation()
        refreshCountries(true)
        observeCountries()

    }


    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            is SignUpEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }

            is SignUpEvent.LastNameChanged -> {
                state = state.copy(lastName = event.lastName)
            }

            is SignUpEvent.PhoneCodeChanged -> {
                state = state.copy(phoneCode = event.phoneCode)
            }

            is SignUpEvent.CellphoneChanged -> {
                state = state.copy(phone = event.phone)
            }

            is SignUpEvent.BirthDateDayChanged -> {
                state = state.copy(birthDateDay = event.birthDateDay)
            }

            is SignUpEvent.BirthDateMonthChanged -> {
                state = state.copy(birthDateMonth = event.birthDateMonth)
            }

            is SignUpEvent.BirthDateYearChanged -> {
                state = state.copy(birthDateYear = event.birthDateYear)
            }

            is SignUpEvent.CityChanged -> {
                state = state.copy(city = event.city)
            }

            is SignUpEvent.DocumentNumberChanged -> {
                state = state.copy(documentNumber = event.documentNumber)
            }

            is SignUpEvent.FrontDocumentUriChanged -> {
                state = state.copy(frontDocumentUri = event.frontDocumentUri)

                if (state.frontDocumentUri != Uri.EMPTY && state.frontDocumentUrl.isEmpty()) {
                    FirebaseStorageService.uploadImage(
                        imageUri = state.frontDocumentUri,
                        username = "test",
                        folder = "documents"
                    ) { frontURLString ->
                        frontURLString?.let {
                            state = state.copy(frontDocumentUrl = it)
                            Log.d(
                                "FirebaseStorageService",
                                "Front Image uploaded successfully: $it"
                            )
                            viewModelScope.launch {
                                userPreferences.saveFrontDocumentUrl(it)
                            }
                            validateDocumentImages()
                        }
                    }
                } else if (state.frontDocumentUrl.isNotEmpty()) {
                    FirebaseStorageService.deleteImage(state.frontDocumentUrl) {
                        state = state.copy(frontDocumentUrl = "")
                        viewModelScope.launch {
                            userPreferences.saveFrontDocumentUrl("")
                        }
                    }
                }

            }

            is SignUpEvent.BackDocumentUriChanged -> {
                state = state.copy(backDocumentUri = event.backDocumentUri)
                if (state.backDocumentUri != Uri.EMPTY && state.backDocumentUrl.isEmpty()) {
                    FirebaseStorageService.uploadImage(
                        imageUri = state.backDocumentUri,
                        username = "test",
                        folder = "documents"
                    ) { backURLString ->
                        backURLString?.let {
                            state = state.copy(backDocumentUrl = it)
                            Log.d("FirebaseStorageService", "Back Image uploaded successfully: $it")
                            viewModelScope.launch {
                                userPreferences.saveBackDocumentUrl(it)
                            }
                            validateDocumentImages()
                        }
                    }
                } else if (state.backDocumentUrl.isNotEmpty()) {
                    FirebaseStorageService.deleteImage(state.backDocumentUrl) {
                        state = state.copy(backDocumentUrl = "")
                        viewModelScope.launch {
                            userPreferences.saveBackDocumentUrl("")
                        }
                    }
                }
            }

            is SignUpEvent.FrontDocumentUrlChanged -> {
                state = state.copy(frontDocumentUrl = event.frontDocumentUrl)
            }

            is SignUpEvent.BackDocumentUrlChanged -> {
                state = state.copy(backDocumentUrl = event.backDocumentUrl)
            }

            is SignUpEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }

            is SignUpEvent.ConfirmPasswordChanged -> {
                state = state.copy(confirmPassword = event.confirmPassword)
            }

            is SignUpEvent.ProfilePictureUriChanged -> {
                state = state.copy(profilePictureUri = event.profilePictureUri)
                if (state.profilePictureUri != Uri.EMPTY && state.profilePictureUrl.isEmpty()) {
                    FirebaseStorageService.uploadImage(
                        imageUri = state.profilePictureUri,
                        username = "test",
                        folder = "profile_pictures"
                    ) { profilePictureURLString ->
                        profilePictureURLString?.let {
                            state = state.copy(profilePictureUrl = it)
                            Log.d(
                                "FirebaseStorageService",
                                "Profile Picture uploaded successfully: $it"
                            )
                            viewModelScope.launch {
                                userPreferences.saveProfilePictureUrl(it)
                            }
                            validateProfilePictureImage()
                        }
                    }
                } else if (state.profilePictureUrl.isNotEmpty()) {
                    FirebaseStorageService.deleteImage(state.profilePictureUrl) {
                        state = state.copy(profilePictureUrl = "")
                        viewModelScope.launch {
                            userPreferences.saveProfilePictureUrl("")
                        }
                    }
                }
            }

            is SignUpEvent.ProfilePictureUrlChanged -> {
                state = state.copy(profilePictureUrl = event.profilePictureUrl)
            }

            is SignUpEvent.ContinueButtonClicked -> {}
            is SignUpEvent.RegisterButtonClicked -> {
                Log.d("Register Button Function", "Register button clicked")
                signUp()
            }
        }
        validateSignUpEmail()
        validateSignUpUserData()
        validateSignUpPhoneData()
        validateSignUpBirthDate()
        validateSignUpCity()
        validateSignUpDocumentNumber()
        validateSignUpPassword()
    }

    fun observeAllLocation() {
        viewModelScope.launch {
            marketLocationUseCases.observeMarketLocations()
                .distinctUntilChanged()
                .collect { locations ->
                    state = state.copy(
                        cities = locations.map { e ->
                            CityState(
                                id = e.id,
                                city = e.city,
                                state = e.state,
                                country = e.country,
                                countryCode = e.countryCode
                            )
                        }
                    )

                    Log.d("SignUpViewModel2", "observeAllLocationTest: ${state.cities}")
                }
        }
    }

    fun updateMarketLocations(fetchFromRemote: Boolean = false) {

        Log.d("SignUpViewModel", "updateMarketLocations")
        updateMarketLocations = viewModelScope.launch {
            val countryName: String = state.phoneCode.let { code ->
                state.countriesByCountryCode[code]
            } ?: ""

            marketLocationUseCases.updateMarketLocations(countryName, fetchFromRemote).catch {
                Log.d("SignUpViewModel", "updateMarketLocations Error: $it");
                state = state.copy(errorMessage = it.message ?: "Unknown error")
            }.collect {
                Log.d("SignUpViewModel", "updateMarketLocations: $it");
            }
        }
    }

    private fun observeCountries() {
        Log.d("SignUpViewModel", "Observing countries...")
        viewModelScope.launch {
            countryUseCases.observeCountries()
                .onStart {
                    state = state.copy(isLoadingCountries = true, countriesError = null)
                }
                .catch { exception ->
                    val errorMessage = when (exception) {
                        is SocketTimeoutException -> "Se agotó el tiempo de espera. Revisa tu conexión a internet."
                        is UnknownHostException -> "No se pudo conectar al servidor. Revisa tu conexión a internet."
                        else -> "Ocurrió un error inesperado: ${exception.message}"
                    }
                    state = state.copy(isLoadingCountries = false, countriesError = errorMessage)
                }
                .collect { countries ->
                    state = state.copy(isLoadingCountries = false, countries = countries)
                }
        }
    }

    private fun refreshCountries(fetchFromRemote: Boolean = false) {
        Log.d("SignUpViewModel", "Refreshing countries...")
        viewModelScope.launch {
            countryUseCases.updateCountries(fetchFromRemote)
                .onStart {
                    state = state.copy(isLoadingCountries = true)
                }
                .catch { exception ->
                    Log.e("SignUpViewModel", "Error refreshing countries", exception)
                    state = state.copy(
                        isLoadingCountries = false,
                        countriesError = "Ocurrió un error inesperado al refrescar."
                    )
                }
                .collect { result: ApiResponse<Unit> ->

                    when (result) {
                        is ApiResponse.Success -> {
                            state = state.copy(isLoadingCountries = false)
                        }

                        is ApiResponse.Error -> {
                            state = state.copy(
                                isLoadingCountries = false,
                                countriesError = result.errorMessage
                            )
                        }

                        is ApiResponse.Failure -> {
                            state = state.copy(
                                isLoadingCountries = false,
                                countriesError = result.errorMessage
                            )
                        }

                        is ApiResponse.Loading -> {
                            state = state.copy(isLoadingCountries = true)
                        }
                    }
                }
        }
    }

    private fun validateSignUpEmail() {
        val emailResult = SignUpValidator.validateEmail(
            email = state.email
        )

        state = state.copy(
            emailError = emailResult.status,
        )

        signUpEmailValidationPassed = emailResult.status
    }

    private fun validateSignUpUserData() {
        val nameResult = SignUpValidator.validateName(
            name = state.name
        )

        val lastNameResult = SignUpValidator.validateLastName(
            lastName = state.lastName
        )

        state = state.copy(
            nameError = nameResult.status,
            lastNameError = lastNameResult.status,
        )

        signUpUserDataValidationPassed = nameResult.status && lastNameResult.status
    }

    private fun validateSignUpPhoneData() {
        val phoneCodeResult = SignUpValidator.validatePhoneCode(
            phoneCode = state.phoneCode
        )

        val cellphoneResult = SignUpValidator.validateCellphone(
            phone = state.phone, phoneCode = state.phoneCode, regex = state.phoneRegex.toRegex()
        )

        state = state.copy(
            phoneCodeError = phoneCodeResult.status,
            phoneError = cellphoneResult.status,
        )


        signUpPhoneDataValidationPassed = phoneCodeResult.status && cellphoneResult.status
    }

    private fun validateSignUpBirthDate() {
        val birthDateResult = SignUpValidator.validateBirthDate(
            birthDate = "${state.birthDateDay} - ${state.birthDateMonth} - ${state.birthDateYear}"
        )

        state = state.copy(
            birthDateError = birthDateResult.status,
        )

        signUpBirthDateValidationPassed = birthDateResult.status
    }

    private fun validateSignUpCity() {
        val cityResult = SignUpValidator.validateCity(
            city = state.city
        )

        state = state.copy(
            cityError = cityResult.status,
        )

        signUpCityValidationPassed = cityResult.status

    }

    private fun validateSignUpDocumentNumber() {
        val documentNumberResult = SignUpValidator.validateDocumentNumber(
            documentNumber = state.documentNumber
        )

        state = state.copy(
            documentNumberError = documentNumberResult.status,
        )

        signUpDocumentNumberValidationPassed = documentNumberResult.status

    }

    private fun validateDocumentImages() {

        val frontDocumentUriResult = SignUpValidator.validateFrontDocumentUri(
            frontDocumentUri = state.frontDocumentUri
        )

        val backDocumentUriResult = SignUpValidator.validateBackDocumentUri(
            backDocumentUri = state.backDocumentUri
        )

        val frontDocumentUrlResult = SignUpValidator.validateFrontDocumentUrl(
            frontDocumentUrl = state.frontDocumentUrl
        )

        val backDocumentUrlResult = SignUpValidator.validateBackDocumentUrl(
            backDocumentUrl = state.backDocumentUrl
        )

        state = state.copy(
            frontDocumentUriError = frontDocumentUriResult.status,
            backDocumentUriError = backDocumentUriResult.status,
            frontDocumentUrlError = frontDocumentUrlResult.status,
            backDocumentUrlError = backDocumentUrlResult.status,
        )

        signUpDocumentImagesValidationPassed =
            frontDocumentUrlResult.status && backDocumentUrlResult.status
    }

    private fun validateSignUpPassword() {
        val passwordResult = SignUpValidator.validatePassword(
            password = state.password
        )

        val confirmPasswordResult = SignUpValidator.validateConfirmPassword(
            confirmPassword = state.confirmPassword,
            password = state.password
        )

        state = state.copy(
            passwordError = passwordResult.status,
            confirmPasswordError = confirmPasswordResult.status
        )

        signUpPasswordValidationPassed = passwordResult.status && confirmPasswordResult.status
    }

    fun validateProfilePictureImage() {
        val profilePictureUriResult = SignUpValidator.validateProfilePictureUri(
            profilePictureUri = state.profilePictureUri
        )

        val profilePictureUrlResult = SignUpValidator.validateProfilePictureUrl(
            profilePictureUrl = state.profilePictureUrl
        )

        state = state.copy(
            profilePictureUriError = profilePictureUriResult.status,
            profilePictureUrlError = profilePictureUrlResult.status
        )

        signUpProfilePictureValidationPassed = profilePictureUrlResult.status
    }

    fun signUp () {
        signUpJob?.cancel()
        signUpJob = viewModelScope.launch {
            val tkn = FirebaseMessaging.getInstance().token.await()
            val SignUpRequest = state.toResponse(tkn)
            Log.d("SignUpRequest", SignUpRequest.toString())
            authUseCases.signUp(SignUpRequest).collect {
                response -> when (response) {
                    is ApiResponse.Success -> {
                        Log.d("SignUpViewModel", "signUp: ${response.data}")
                        state = state.copy(
                            isSignedUp = true,
                            signUpResponse = response.data.message
                        )
                    }

                    is ApiResponse.Error -> {
                        Log.d("SignUpError", "signUp: ${response.errorMessage}")
                        state = state.copy(
                            signUpError = true,
                            errorMessage = response.errorMessage
                        )
                    }

                    is ApiResponse.Failure -> {
                        Log.d("SignUpFailure", "signUp: ${response.errorMessage}")
                        state = state.copy(
                            signUpError = true,
                            errorMessage = response.errorMessage
                        )
                    }

                    is ApiResponse.Loading -> {
                    }
                }
            }
        }

    }

    fun resetSignUpGlobalValidation () {
        state = state.copy(
            signUpError = false,
            errorMessage = ""
        )
    }

    override fun onCleared() {
        super.onCleared()
        signUpJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        Log.d("Cleared", "onCleared")
    }

}
