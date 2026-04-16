package edu.ucne.dulcedeleite.presentation.mispedidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.data.local.datastore.AuthTokenManager
import edu.ucne.dulcedeleite.domain.usecase.pedido.ObtenerPedidosUseCase
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MisPedidosViewModel @Inject constructor(
    private val obtenerPedidosUseCase: ObtenerPedidosUseCase,
    private val authTokenManager: AuthTokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MisPedidosUiState())
    val uiState: StateFlow<MisPedidosUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: MisPedidosUiEvent) {
        when (event) {
            MisPedidosUiEvent.Refresh -> {
                loadData()
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            authTokenManager.getUsuarioId().collect { userId ->
                if (userId != null && userId > 0) {
                    fetchPedidos(userId)
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Usuario no autenticado") }
                }
            }
        }
    }

    private fun fetchPedidos(userId: Int) {
        obtenerPedidosUseCase(userId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                is Resource.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            pedidos = result.data?.sortedByDescending { p -> p.fecha } ?: emptyList()
                        ) 
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }.launchIn(viewModelScope)
    }
}
