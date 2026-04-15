package edu.ucne.dulcedeleite.presentation.proyecto.edit

sealed class ProductoEditUiEvent {
    data class OnNombreChanged(val nombre: String) : ProductoEditUiEvent()
    data class OnDescripcionChanged(val descripcion: String) : ProductoEditUiEvent()
    data class OnPrecioChanged(val precio: String) : ProductoEditUiEvent()
    data class OnStockChanged(val stock: String) : ProductoEditUiEvent()
    data class OnImagenUrlChanged(val imagenUrl: String) : ProductoEditUiEvent()
    object OnSave : ProductoEditUiEvent()
}
