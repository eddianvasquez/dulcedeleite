package edu.ucne.dulcedeleite.presentation.proyecto.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.domain.repository.ProductoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoListViewModel @Inject constructor(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeProductos()
        refreshProductos()
    }

    private fun observeProductos() {
        viewModelScope.launch {
            repository.observeProductos().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(isLoading = false, productos = result.data ?: emptyList()) 
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(isLoading = false, errorMessage = result.message) 
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun refreshProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.refreshProductos()
            // observeProductos se encargará de actualizar el estado al obtener los nuevos datos
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun deleteProducto(id: Int) {
        viewModelScope.launch {
            repository.deleteProducto(id)
            // Error handling se podría enviar por events (SharedFlow) si se requiere
        }
    }
}
