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
}