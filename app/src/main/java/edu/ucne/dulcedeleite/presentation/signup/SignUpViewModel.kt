package edu.ucne.dulcedeleite.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.data.remote.dto.RegistroUsuarioDto
import edu.ucne.dulcedeleite.domain.usecase.SignUpUseCase
import edu.ucne.dulcedeleite.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.NombreChanged -> {
                _uiState.update { it.copy(nombre = event.value, nombreError = null) }
            }
            is SignUpUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.value, emailError = null) }
            }
            is SignUpUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.value, passwordError = null) }
            }
            is SignUpUiEvent.TelefonoChanged -> {
                _uiState.update { it.copy(telefono = event.value, telefonoError = null) }
            }
            SignUpUiEvent.Submit -> {
                submitData()
            }
            SignUpUiEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun submitData() {
        val state = uiState.value
        val nombreError = if (state.nombre.isBlank()) "Campo requerido" else null
        val emailError = if (state.email.isBlank()) "Campo requerido" else null
        val passwordError = if (state.password.isBlank()) "Campo requerido" else null
        val telefonoError = if (state.telefono.isBlank()) "Campo requerido" else null

        if (nombreError != null || emailError != null || passwordError != null || telefonoError != null) {
            _uiState.update {
                it.copy(
                    nombreError = nombreError,
                    emailError = emailError,
                    passwordError = passwordError,
                    telefonoError = telefonoError
                )
            }
            return
        }

        signUpUseCase(
            RegistroUsuarioDto(
                nombre = state.nombre,
                email = state.email,
                password = state.password,
                telefono = state.telefono
            )
        ).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                }
                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true, errorMessage = null)
                    }
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, signUpSuccess = true)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}
