package edu.ucne.dulcedeleite.data.remote.dto

import edu.ucne.dulcedeleite.domain.model.Producto

data class ProductoDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val imagenUrl: String
) {
    // Función de mapeo como enseña tu maestro
    fun toDomain() = Producto(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        stock = stock,
        imagenUrl = imagenUrl
    )
}