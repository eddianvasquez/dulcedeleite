package edu.ucne.dulcedeleite.domain.repository

import edu.ucne.dulcedeleite.domain.model.Producto
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    // Retorna un Flow para que la UI se actualice automáticamente (Room)
    fun observeProductos(): Flow<Resource<List<Producto>>>
    suspend fun refreshProductos() // Función para forzar la descarga desde Somee

    suspend fun createProducto(producto: Producto): Resource<Producto>
    suspend fun updateProducto(id: Int, producto: Producto): Resource<Producto>
    suspend fun deleteProducto(id: Int): Resource<Unit>

    suspend fun uploadImage(file: okhttp3.MultipartBody.Part): Resource<edu.ucne.dulcedeleite.data.remote.dto.UploadResponseDto>
}