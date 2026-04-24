package edu.ucne.dulcedeleite.presentation.direccion.form

data class DireccionFormUiState(
    val id: Int = 0,
    val usuarioId: Int = 0,
    val calle: String = "",
    val ciudad: String = "",
    val referencia: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

sealed interface DireccionFormUiEvent {
    data class CalleChanged(val calle: String) : DireccionFormUiEvent
    data class CiudadChanged(val ciudad: String) : DireccionFormUiEvent
    data class ReferenciaChanged(val referencia: String) : DireccionFormUiEvent
    data object Submit : DireccionFormUiEvent
    data class LoadDireccion(val id: Int) : DireccionFormUiEvent
}
