package edu.ucne.dulcedeleite.domain.usecase.direccion

import edu.ucne.dulcedeleite.domain.model.Direccion
import edu.ucne.dulcedeleite.domain.repository.DireccionRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveDireccionUseCase @Inject constructor(
    private val repository: DireccionRepository
) {
    operator fun invoke(direccion: Direccion): Flow<Resource<Direccion>> = flow {
        emit(Resource.Loading())
        val result = if (direccion.id == 0) {
            repository.createDireccion(direccion)
        } else {
            repository.updateDireccion(direccion.id, direccion)
        }

        if (result is Resource.Success) {
            emit(Resource.Success(result.data!!))
        } else {
            emit(Resource.Error(result.message ?: "Error desconocido"))
        }
    }
}
