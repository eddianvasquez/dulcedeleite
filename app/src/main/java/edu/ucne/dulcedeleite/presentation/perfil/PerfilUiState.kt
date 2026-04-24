package edu.ucne.dulcedeleite.presentation.perfil

data class PerfilUiState(
    val nombre: String = "",
    val correo: String = ""
)

sealed interface PerfilUiEvent {
    data object CerrarSesion : PerfilUiEvent
}
