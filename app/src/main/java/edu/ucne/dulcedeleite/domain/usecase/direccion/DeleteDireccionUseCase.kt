package edu.ucne.dulcedeleite.domain.usecase.direccion

import edu.ucne.dulcedeleite.domain.repository.DireccionRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteDireccionUseCase @Inject constructor(
    private val repository: DireccionRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val result = repository.deleteDireccion(id)
        if (result is Resource.Success) {
            emit(Resource.Success(Unit))
        } else {
            emit(Resource.Error(result.message ?: "Error desconocido"))
        }
    }
}
