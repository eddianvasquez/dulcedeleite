package edu.ucne.dulcedeleite.presentation.proyecto.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.domain.model.Producto
import edu.ucne.dulcedeleite.domain.repository.ProductoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import edu.ucne.dulcedeleite.domain.usecase.UploadImageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoEditViewModel @Inject constructor(
    private val repository: ProductoRepository,
    private val uploadImageUseCase: UploadImageUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoEditUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val productoId = savedStateHandle.get<Int>("id") ?: 0
        if (productoId != 0) {
            _uiState.update { it.copy(id = productoId, isLoading = true) }
            loadProducto(productoId)
        }
    }

    private fun loadProducto(id: Int) {
        viewModelScope.launch {
            // Buscamos temporalmente todos los productos para encontrar el actual y llenar el form (EnRoom u observe)
            repository.observeProductos().collect { resource ->
                if (resource is Resource.Success) {
                    val producto = resource.data?.find { it.id == id }
                    producto?.let { prod ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                nombre = prod.nombre,
                                descripcion = prod.descripcion,
                                precio = prod.precio.toString(),
                                stock = prod.stock.toString(),
                                imagenUrl = prod.imagenUrl
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ProductoEditUiEvent) {
        when (event) {
            is ProductoEditUiEvent.OnNombreChanged -> {
                _uiState.update { it.copy(nombre = event.nombre, nombreError = null) }
            }
            is ProductoEditUiEvent.OnDescripcionChanged -> {
                _uiState.update { it.copy(descripcion = event.descripcion, descripcionError = null) }
            }
            is ProductoEditUiEvent.OnPrecioChanged -> {
                _uiState.update { it.copy(precio = event.precio, precioError = null) }
            }
            is ProductoEditUiEvent.OnStockChanged -> {
                _uiState.update { it.copy(stock = event.stock, stockError = null) }
            }
            is ProductoEditUiEvent.OnImagenUrlChanged -> {
                _uiState.update { it.copy(imagenUrl = event.imagenUrl, imagenUrlError = null) }
            }
            ProductoEditUiEvent.OnSave -> {
                saveProducto()
            }
        }
    }

    private fun saveProducto() {
        val state = _uiState.value
        if (state.nombre.isBlank()) {
            _uiState.update { it.copy(nombreError = "El nombre es obligatorio") }
            return
        }
        val precioNum = state.precio.toDoubleOrNull()
        if (precioNum == null) {
            _uiState.update { it.copy(precioError = "Precio inválido") }
            return
        }
        val stockNum = state.stock.toIntOrNull()
        if (stockNum == null) {
            _uiState.update { it.copy(stockError = "Stock inválido") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

        viewModelScope.launch {
            var urlParaGuardar = state.imagenUrl

            // 1. Verificar si la imagen es local y necesita subirse
            if (urlParaGuardar.startsWith("content://")) {
                val uploadResult = uploadImageUseCase(urlParaGuardar)
                when (uploadResult) {
                    is Resource.Success -> {
                        // El backend devuelve algo como "/images/123.jpg" u "http://..."
                        val urlParcial = uploadResult.data?.url ?: ""
                        urlParaGuardar = if (urlParcial.startsWith("http")) urlParcial else "http://DulceDeleite.somee.com$urlParcial"
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Error subiendo imagen: ${uploadResult.message}") }
                        return@launch
                    }
                    is Resource.Loading -> Unit
                }
            }

            val producto = Producto(
                id = state.id,
                nombre = state.nombre,
                descripcion = state.descripcion,
                precio = precioNum,
                stock = stockNum,
                imagenUrl = urlParaGuardar
            )

            val result = if (producto.id == 0) {
                repository.createProducto(producto)
            } else {
                repository.updateProducto(producto.id, producto)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, successMessage = "Producto guardado exitosamente") }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message ?: "Error al guardar") }
                }
                is Resource.Loading -> Unit
            }
        }
    }
}
