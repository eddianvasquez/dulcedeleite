package edu.ucne.dulcedeleite.presentation.signup

sealed class SignUpUiEvent {
    data class NombreChanged(val value: String) : SignUpUiEvent()
    data class EmailChanged(val value: String) : SignUpUiEvent()
    data class PasswordChanged(val value: String) : SignUpUiEvent()
    data class TelefonoChanged(val value: String) : SignUpUiEvent()
    object Submit : SignUpUiEvent()
    object ClearError : SignUpUiEvent()
}
