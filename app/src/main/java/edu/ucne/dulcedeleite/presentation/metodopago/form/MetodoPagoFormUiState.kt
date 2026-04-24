package edu.ucne.dulcedeleite.presentation.metodopago.form

data class MetodoPagoFormUiState(
    val id: Int = 0,
    val nombre: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

sealed interface MetodoPagoFormUiEvent {
    data class NombreChanged(val nombre: String) : MetodoPagoFormUiEvent
    data object Submit : MetodoPagoFormUiEvent
    data class LoadMetodoPago(val id: Int) : MetodoPagoFormUiEvent
}
