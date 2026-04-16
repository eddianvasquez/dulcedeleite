package edu.ucne.dulcedeleite.presentation.carrito

import edu.ucne.dulcedeleite.domain.model.Producto

data class CartItem(
    val producto: Producto,
    val cantidad: Int
)
