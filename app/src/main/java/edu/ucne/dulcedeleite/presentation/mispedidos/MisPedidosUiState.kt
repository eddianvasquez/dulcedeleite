package edu.ucne.dulcedeleite.presentation.mispedidos

import edu.ucne.dulcedeleite.data.remote.dto.PedidoDto

data class MisPedidosUiState(
    val isLoading: Boolean = false,
    val pedidos: List<PedidoDto> = emptyList(),
    val errorMessage: String? = null
)
