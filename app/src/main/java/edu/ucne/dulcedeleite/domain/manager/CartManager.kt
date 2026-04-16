package edu.ucne.dulcedeleite.domain.manager

import edu.ucne.dulcedeleite.presentation.carrito.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartManager @Inject constructor() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    fun agregarProducto(item: CartItem) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.producto.id == item.producto.id }
            if (existingItem != null) {
                currentList.map { 
                    if (it.producto.id == item.producto.id) {
                        it.copy(cantidad = it.cantidad + item.cantidad)
                    } else {
                        it
                    }
                }
            } else {
                currentList + item
            }
        }
    }
    
    fun actualizarCantidad(productoId: Int, incremento: Int) {
        _cartItems.update { currentList ->
            currentList.mapNotNull { item ->
                if (item.producto.id == productoId) {
                    val nuevaCantidad = item.cantidad + incremento
                    if (nuevaCantidad > 0) {
                        item.copy(cantidad = nuevaCantidad)
                    } else {
                        null
                    }
                } else {
                    item
                }
            }
        }
    }
    
    fun eliminarProducto(productoId: Int) {
        _cartItems.update { currentList ->
            currentList.filter { it.producto.id != productoId }
        }
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
