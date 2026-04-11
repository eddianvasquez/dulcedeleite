package edu.ucne.dulcedeleite.domain.repository

import edu.ucne.dulcedeleite.domain.model.Producto
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    // Retorna un Flow para que la UI se actualice automáticamente (Room)
    fun observeProductos(): Flow<Resource<List<Producto>>>
    suspend fun refreshProductos() // Función para forzar la descarga desde Somee
}