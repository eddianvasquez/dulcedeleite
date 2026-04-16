package edu.ucne.dulcedeleite.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.data.remote.dto.LoginDto
import edu.ucne.dulcedeleite.domain.usecase.LoginUseCase
import edu.ucne.dulcedeleite.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

import edu.ucne.dulcedeleite.data.local.datastore.AuthTokenManager
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: AuthTokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.value, emailError = null) }
            }
            is LoginUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.value, passwordError = null) }
            }
            LoginUiEvent.Submit -> {
                submitData()
            }
            LoginUiEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun submitData() {
        val emailError = if (uiState.value.email.isBlank()) "No puede estar vacío" else null
        val passwordError = if (uiState.value.password.isBlank()) "No puede estar vacío" else null

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(emailError = emailError, passwordError = passwordError)
            }
            return
        }

        loginUseCase(
            LoginDto(
                email = uiState.value.email,
                password = uiState.value.password
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
                        it.copy(isLoading = false, loginSuccess = true)
                    }
                    viewModelScope.launch {
                        val authData = result.data
                        if (authData?.token != null) {
                            tokenManager.saveTokenAndRole(
                                token = authData.token,
                                rol = authData.rol,
                                usuarioId = authData.usuarioId,
                                nombre = authData.nombre,
                                correo = uiState.value.email
                            )
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}
