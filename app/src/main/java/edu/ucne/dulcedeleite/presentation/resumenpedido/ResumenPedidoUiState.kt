package edu.ucne.dulcedeleite.presentation.resumenpedido

import edu.ucne.dulcedeleite.data.remote.dto.DireccionDto
import edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto
import edu.ucne.dulcedeleite.presentation.carrito.CartItem
import java.time.LocalDate
import java.time.LocalTime

data class ResumenPedidoUiState(
    val isLoading: Boolean = false,
    val isLoadingData: Boolean = false,
    val cartItems: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val errorMessage: String? = null,
    val currentUserId: Int? = null,
    
    val direcciones: List<DireccionDto> = emptyList(),
    val selectedDireccion: DireccionDto? = null,
    
    val metodosPago: List<MetodoPagoDto> = emptyList(),
    val selectedMetodoPago: MetodoPagoDto? = null,
    
    val mensajePersonalizado: String = "",
    val fechaEntrega: LocalDate? = null,
    val horaEntrega: LocalTime? = null,
    
    val isPostSuccess: Boolean = false,
    
    // UI controls for bottom sheets
    val showDireccionSheet: Boolean = false,
    val showMetodoPagoSheet: Boolean = false
)
