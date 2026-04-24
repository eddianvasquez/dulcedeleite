package edu.ucne.dulcedeleite.presentation.proyecto.list

sealed interface ProductoListUiEvent {
    object Refresh : ProductoListUiEvent
    data class Delete(val id: Int) : ProductoListUiEvent
    data class AddToCart(val producto: edu.ucne.dulcedeleite.domain.model.Producto) : ProductoListUiEvent
}
