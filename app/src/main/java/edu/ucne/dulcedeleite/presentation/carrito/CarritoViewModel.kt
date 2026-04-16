package edu.ucne.dulcedeleite.presentation.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.domain.manager.CartManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val cartManager: CartManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    private val _navigateToCheckoutEvent = MutableSharedFlow<Unit>()
    val navigateToCheckoutEvent: SharedFlow<Unit> = _navigateToCheckoutEvent.asSharedFlow()

    init {
        cartManager.cartItems.onEach { items ->
            _uiState.update { 
                it.copy(
                    items = items,
                    totalPrecio = items.sumOf { item -> item.producto.precio * item.cantidad }
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: CarritoUiEvent) {
        when (event) {
            is CarritoUiEvent.AumentarCantidad -> {
                cartManager.actualizarCantidad(event.productoId, 1)
            }
            is CarritoUiEvent.DisminuirCantidad -> {
                cartManager.actualizarCantidad(event.productoId, -1)
            }
            is CarritoUiEvent.EliminarProducto -> {
                cartManager.eliminarProducto(event.productoId)
            }
            CarritoUiEvent.ProcederAlPago -> {
                viewModelScope.launch {
                    if (_uiState.value.items.isNotEmpty()) {
                        _navigateToCheckoutEvent.emit(Unit)
                    }
                }
            }
        }
    }
}
