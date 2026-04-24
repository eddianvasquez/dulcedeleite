package edu.ucne.dulcedeleite.data.repository

import edu.ucne.dulcedeleite.data.remote.dto.DireccionDto
import edu.ucne.dulcedeleite.data.remote.remotedatasource.DulceDeleiteRemoteDataSource
import edu.ucne.dulcedeleite.domain.model.Direccion
import edu.ucne.dulcedeleite.domain.repository.DireccionRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DireccionRepositoryImpl @Inject constructor(
    private val remoteDataSource: DulceDeleiteRemoteDataSource
) : DireccionRepository {

    override fun getDirecciones(): Flow<Resource<List<Direccion>>> = flow {
        emit(Resource.Loading())
        val result = remoteDataSource.getDirecciones()
        if (result.isSuccess) {
            val list = result.getOrNull()?.map { it.toDomain() } ?: emptyList()
            emit(Resource.Success(list))
        } else {
            emit(Resource.Error(result.exceptionOrNull()?.message ?: "Error desconocido"))
        }
    }

    override fun getDireccion(id: Int): Flow<Resource<Direccion>> = flow {
        emit(Resource.Loading())
        val result = remoteDataSource.getDireccion(id)
        if (result.isSuccess) {
            result.getOrNull()?.let { emit(Resource.Success(it.toDomain())) }
        } else {
            emit(Resource.Error(result.exceptionOrNull()?.message ?: "Error desconocido"))
        }
    }

    override suspend fun createDireccion(direccion: Direccion): Resource<Direccion> {
        val result = remoteDataSource.createDireccion(direccion.toDto())
        return if (result.isSuccess) {
            Resource.Success(result.getOrThrow().toDomain())
        } else {
            Resource.Error(result.exceptionOrNull()?.message ?: "Error creando dirección")
        }
    }

    override suspend fun updateDireccion(id: Int, direccion: Direccion): Resource<Direccion> {
        val result = remoteDataSource.updateDireccion(id, direccion.toDto())
        return if (result.isSuccess) {
            Resource.Success(result.getOrThrow().toDomain())
        } else {
            Resource.Error(result.exceptionOrNull()?.message ?: "Error actualizando dirección")
        }
    }

    override suspend fun deleteDireccion(id: Int): Resource<Unit> {
        val result = remoteDataSource.deleteDireccion(id)
        return if (result.isSuccess) {
            Resource.Success(Unit)
        } else {
            Resource.Error(result.exceptionOrNull()?.message ?: "Error eliminando dirección")
        }
    }

    private fun DireccionDto.toDomain() = Direccion(
        id = id,
        usuarioId = usuarioId,
        calle = calle,
        ciudad = ciudad,
        referencia = referencia
    )

    private fun Direccion.toDto() = DireccionDto(
        id = id,
        usuarioId = usuarioId,
        calle = calle,
        ciudad = ciudad,
        referencia = referencia
    )
}
