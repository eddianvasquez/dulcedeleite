package edu.ucne.dulcedeleite.data.repository

import edu.ucne.dulcedeleite.data.local.dao.DulceDeleiteDao
import edu.ucne.dulcedeleite.data.local.entity.toDomain
import edu.ucne.dulcedeleite.data.local.entity.toEntity
import edu.ucne.dulcedeleite.data.remote.remotedatasource.DulceDeleiteRemoteDataSource
import edu.ucne.dulcedeleite.domain.model.Producto
import edu.ucne.dulcedeleite.domain.repository.ProductoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    private val localDataSource: DulceDeleiteDao,
    private val remoteDataSource: DulceDeleiteRemoteDataSource
) : ProductoRepository {

    override fun observeProductos(): Flow<Resource<List<Producto>>> {
        return localDataSource.observeProductos().map { entities ->
            Resource.Success(entities.map { it.toDomain() })
        }
    }

    override suspend fun refreshProductos() {
        val result = remoteDataSource.getProductos()
        if (result.isSuccess) {
            val productosAPI = result.getOrNull() ?: emptyList()
            localDataSource.clearProductos()
            localDataSource.upsertProductos(productosAPI.map { it.toDomain().toEntity() })
        }
    }

    override suspend fun createProducto(producto: Producto): Resource<Producto> {
        return try {
            val dto = edu.ucne.dulcedeleite.data.remote.dto.ProductoDto(
                id = producto.id,
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                precio = producto.precio,
                stock = producto.stock,
                imagenUrl = producto.imagenUrl
            )
            val result = remoteDataSource.createProducto(dto)
            if (result.isSuccess) {
                val createdDto = result.getOrThrow()
                localDataSource.upsertProductos(listOf(createdDto.toDomain().toEntity()))
                Resource.Success(createdDto.toDomain())
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "Error al crear producto")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error inesperado")
        }
    }

    override suspend fun updateProducto(id: Int, producto: Producto): Resource<Producto> {
        return try {
            val dto = edu.ucne.dulcedeleite.data.remote.dto.ProductoDto(
                id = producto.id,
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                precio = producto.precio,
                stock = producto.stock,
                imagenUrl = producto.imagenUrl
            )
            val result = remoteDataSource.updateProducto(id, dto)
            if (result.isSuccess) {
                val updatedDto = result.getOrThrow()
                localDataSource.upsertProductos(listOf(updatedDto.toDomain().toEntity()))
                Resource.Success(updatedDto.toDomain())
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "Error al actualizar producto")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error inesperado")
        }
    }

    override suspend fun deleteProducto(id: Int): Resource<Unit> {
        return try {
            val result = remoteDataSource.deleteProducto(id)
            if (result.isSuccess) {
                refreshProductos() // Refresh local DB after delete
                Resource.Success(Unit)
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "Error al eliminar producto")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error inesperado")
        }
    }

    override suspend fun uploadImage(file: okhttp3.MultipartBody.Part): Resource<edu.ucne.dulcedeleite.data.remote.dto.UploadResponseDto> {
        return try {
            val result = remoteDataSource.uploadImage(file)
            if (result.isSuccess) {
                Resource.Success(result.getOrThrow())
            } else {
                Resource.Error(result.exceptionOrNull()?.message ?: "Error al subir la imagen")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error inesperado")
        }
    }
}