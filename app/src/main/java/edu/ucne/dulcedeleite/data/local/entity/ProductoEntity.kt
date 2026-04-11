package edu.ucne.dulcedeleite.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.ucne.dulcedeleite.domain.model.Producto

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val imagenUrl: String
)

// Funciones de Mapeo (Mapper)
fun ProductoEntity.toDomain() = Producto(id, nombre, descripcion, precio, stock, imagenUrl)
fun Producto.toEntity() = ProductoEntity(id, nombre, descripcion, precio, stock, imagenUrl)