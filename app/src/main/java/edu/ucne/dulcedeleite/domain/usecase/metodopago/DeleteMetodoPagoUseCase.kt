package edu.ucne.dulcedeleite.domain.usecase.metodopago

import edu.ucne.dulcedeleite.domain.repository.MetodoPagoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteMetodoPagoUseCase @Inject constructor(
    private val repository: MetodoPagoRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val result = repository.deleteMetodoPago(id)
        if (result is Resource.Success) {
            emit(Resource.Success(Unit))
        } else {
            emit(Resource.Error(result.message ?: "Error desconocido"))
        }
    }
}
