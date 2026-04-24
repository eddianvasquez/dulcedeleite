package edu.ucne.dulcedeleite.presentation.resumenpedido

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.data.local.datastore.AuthTokenManager
import edu.ucne.dulcedeleite.data.remote.dto.DetallePedidoDto
import edu.ucne.dulcedeleite.data.remote.dto.PedidoDto
import edu.ucne.dulcedeleite.domain.manager.CartManager
import edu.ucne.dulcedeleite.domain.repository.DireccionRepository
import edu.ucne.dulcedeleite.domain.repository.MetodoPagoRepository
import edu.ucne.dulcedeleite.domain.usecase.pedido.CrearPedidoUseCase
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ResumenPedidoViewModel @Inject constructor(
    private val crearPedidoUseCase: CrearPedidoUseCase,
    private val direccionRepository: DireccionRepository,
    private val metodoPagoRepository: MetodoPagoRepository,
    private val cartManager: CartManager,
    private val authTokenManager: AuthTokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResumenPedidoUiState())
    val uiState: StateFlow<ResumenPedidoUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingData = true) }

            // Cargar usuario
            val userId = authTokenManager.getUsuarioId().first()

            // Observamos el carrito para el resumen
            cartManager.cartItems.collect { items ->
                val total = items.sumOf { it.producto.precio * it.cantidad }
                _uiState.update { it.copy(cartItems = items, total = total, currentUserId = userId) }
            }
        }
        
        loadDirecciones()
        loadMetodosPago()
    }

    private fun loadDirecciones() {
        viewModelScope.launch {
            direccionRepository.getDirecciones().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val dirs = result.data?.map { 
                            edu.ucne.dulcedeleite.data.remote.dto.DireccionDto(it.id, it.usuarioId, it.calle, it.ciudad, it.referencia) 
                        } ?: emptyList()
                        _uiState.update { state ->
                            state.copy(
                                direcciones = dirs,
                                selectedDireccion = state.selectedDireccion ?: dirs.firstOrNull(),
                                isLoadingData = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(errorMessage = result.message, isLoadingData = false) }
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun loadMetodosPago() {
        viewModelScope.launch {
            metodoPagoRepository.getMetodosPago().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val methods = result.data?.map {
                            edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto(it.id, it.nombre)
                        } ?: emptyList()
                        _uiState.update { state ->
                            state.copy(
                                metodosPago = methods,
                                selectedMetodoPago = state.selectedMetodoPago ?: methods.firstOrNull(),
                                isLoadingData = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(errorMessage = result.message, isLoadingData = false) }
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: ResumenPedidoUiEvent) {
        when (event) {
            is ResumenPedidoUiEvent.ToggleDireccionSheet -> {
                _uiState.update { it.copy(showDireccionSheet = event.show) }
            }
            is ResumenPedidoUiEvent.ToggleMetodoPagoSheet -> {
                _uiState.update { it.copy(showMetodoPagoSheet = event.show) }
            }
            is ResumenPedidoUiEvent.SelectDireccion -> {
                _uiState.update { it.copy(selectedDireccion = event.direccion, showDireccionSheet = false) }
            }
            is ResumenPedidoUiEvent.SelectMetodoPago -> {
                _uiState.update { it.copy(selectedMetodoPago = event.metodoPago, showMetodoPagoSheet = false) }
            }
            is ResumenPedidoUiEvent.UpdateMensaje -> {
                _uiState.update { it.copy(mensajePersonalizado = event.mensaje) }
            }
            is ResumenPedidoUiEvent.UpdateFecha -> {
                _uiState.update { it.copy(fechaEntrega = event.fecha) }
            }
            is ResumenPedidoUiEvent.UpdateHora -> {
                _uiState.update { it.copy(horaEntrega = event.hora) }
            }
            ResumenPedidoUiEvent.ConfirmarPedido -> {
                confirmarPedido()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun confirmarPedido() {
        val state = uiState.value
        val userId = state.currentUserId ?: return
        val direccion = state.selectedDireccion ?: return
        val metodoPago = state.selectedMetodoPago ?: return
        
        if (state.cartItems.isEmpty()) return

        val detalles = state.cartItems.map { item ->
            DetallePedidoDto(
                id = 0,
                pedidoId = 0,
                productoId = item.producto.id,
                cantidad = item.cantidad,
                precioUnitario = item.producto.precio
            )
        }

        // Simular fecha de entrega actual si no se seleccionó
        val fechaString = if (state.fechaEntrega != null) {
            state.fechaEntrega.format(DateTimeFormatter.ISO_LOCAL_DATE) + "T" + (state.horaEntrega?.format(DateTimeFormatter.ISO_LOCAL_TIME) ?: "00:00:00") + "Z"
        } else {
            DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        }

        val pedidoDto = PedidoDto(
            id = 0,
            usuarioId = userId,
            fecha = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
            estado = "Pendiente",
            total = state.total,
            direccionId = direccion.id,
            metodoPagoId = metodoPago.id,
            detalles = detalles
        )

        viewModelScope.launch {
            crearPedidoUseCase(pedidoDto).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        cartManager.clearCart()
                        _uiState.update { it.copy(isLoading = false, isPostSuccess = true) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                }
            }
        }
    }
}
