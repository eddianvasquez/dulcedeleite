package edu.ucne.dulcedeleite.presentation.mispedidos

sealed interface MisPedidosUiEvent {
    data object Refresh : MisPedidosUiEvent
}
