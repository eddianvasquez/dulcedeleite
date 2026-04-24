package edu.ucne.dulcedeleite.presentation.direccion.list

import edu.ucne.dulcedeleite.domain.model.Direccion

data class DireccionListUiState(
    val isLoading: Boolean = false,
    val direcciones: List<Direccion> = emptyList(),
    val errorMessage: String? = null
)

sealed interface DireccionListUiEvent {
    data class DeleteDireccion(val id: Int) : DireccionListUiEvent
    data object RefreshDefault : DireccionListUiEvent
}
