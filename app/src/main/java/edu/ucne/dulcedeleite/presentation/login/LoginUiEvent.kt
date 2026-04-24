package edu.ucne.dulcedeleite.presentation.login

sealed class LoginUiEvent {
    data class EmailChanged(val value: String) : LoginUiEvent()
    data class PasswordChanged(val value: String) : LoginUiEvent()
    object Submit : LoginUiEvent()
    object ClearError : LoginUiEvent()
}
