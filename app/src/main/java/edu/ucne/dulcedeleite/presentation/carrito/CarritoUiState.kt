package edu.ucne.dulcedeleite.presentation.carrito

data class CarritoUiState(
    val items: List<CartItem> = emptyList(),
    val totalPrecio: Double = 0.0
)

sealed interface CarritoUiEvent {
    data class AumentarCantidad(val productoId: Int) : CarritoUiEvent
    data class DisminuirCantidad(val productoId: Int) : CarritoUiEvent
    data class EliminarProducto(val productoId: Int) : CarritoUiEvent
    data object ProcederAlPago : CarritoUiEvent
}
