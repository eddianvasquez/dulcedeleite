package edu.ucne.dulcedeleite.presentation.metodopago.list

import edu.ucne.dulcedeleite.domain.model.MetodoPago

data class MetodoPagoListUiState(
    val isLoading: Boolean = false,
    val metodosPago: List<MetodoPago> = emptyList(),
    val errorMessage: String? = null
)

sealed interface MetodoPagoListUiEvent {
    data class DeleteMetodoPago(val id: Int) : MetodoPagoListUiEvent
    data object RefreshDefault : MetodoPagoListUiEvent
}
