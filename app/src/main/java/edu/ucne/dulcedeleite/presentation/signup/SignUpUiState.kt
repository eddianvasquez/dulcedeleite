package edu.ucne.dulcedeleite.presentation.signup

data class SignUpUiState(
    val nombre: String = "",
    val nombreError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val telefono: String = "",
    val telefonoError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val signUpSuccess: Boolean = false
)
