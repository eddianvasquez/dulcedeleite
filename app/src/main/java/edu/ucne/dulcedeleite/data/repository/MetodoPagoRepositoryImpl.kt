package edu.ucne.dulcedeleite.data.repository

import edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto
import edu.ucne.dulcedeleite.data.remote.remotedatasource.DulceDeleiteRemoteDataSource
import edu.ucne.dulcedeleite.domain.model.MetodoPago
import edu.ucne.dulcedeleite.domain.repository.MetodoPagoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MetodoPagoRepositoryImpl @Inject constructor(
    private val remoteDataSource: DulceDeleiteRemoteDataSource
) : MetodoPagoRepository {

    override fun getMetodosPago(): Flow<Resource<List<MetodoPago>>> = flow {
        emit(Resource.Loading())
        val result = remoteDataSource.getMetodosPago()
        if (result.isSuccess) {
            val list = result.getOrNull()?.map { it.toDomain() } ?: emptyList()
            emit(Resource.Success(list))
        } else {
            emit(Resource.Error(result.exceptionOrNull()?.message ?: "Error desconocido"))
        }
    }

    override fun getMetodoPago(id: Int): Flow<Resource<MetodoPago>> = flow {
        emit(Resource.Loading())
        val result = remoteDataSource.getMetodoPago(id)
        if (result.isSuccess) {
            result.getOrNull()?.let { emit(Resource.Success(it.toDomain())) }
        } else {
            emit(Resource.Error(result.exceptionOrNull()?.message ?: "Error desconocido"))
        }
    }

    override suspend fun createMetodoPago(metodoPago: MetodoPago): Resource<MetodoPago> {
        val result = remoteDataSource.createMetodoPago(metodoPago.toDto())
        return if (result.isSuccess) {
            Resource.Success(result.getOrThrow().toDomain())
        } else {
            Resource.Error(result.exceptionOrNull()?.message ?: "Error creando método de pago")
        }
    }

    override suspend fun updateMetodoPago(id: Int, metodoPago: MetodoPago): Resource<MetodoPago> {
        val result = remoteDataSource.updateMetodoPago(id, metodoPago.toDto())
        return if (result.isSuccess) {
            Resource.Success(result.getOrThrow().toDomain())
        } else {
            Resource.Error(result.exceptionOrNull()?.message ?: "Error actualizando método de pago")
        }
    }

    override suspend fun deleteMetodoPago(id: Int): Resource<Unit> {
        val result = remoteDataSource.deleteMetodoPago(id)
        return if (result.isSuccess) {
            Resource.Success(Unit)
        } else {
            Resource.Error(result.exceptionOrNull()?.message ?: "Error eliminando método de pago")
        }
    }

    private fun MetodoPagoDto.toDomain() = MetodoPago(
        id = id,
        nombre = nombre
    )

    private fun MetodoPago.toDto() = MetodoPagoDto(
        id = id,
        nombre = nombre
    )
}
