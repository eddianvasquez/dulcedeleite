package edu.ucne.dulcedeleite.domain.usecase.metodopago

import edu.ucne.dulcedeleite.domain.model.MetodoPago
import edu.ucne.dulcedeleite.domain.repository.MetodoPagoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveMetodoPagoUseCase @Inject constructor(
    private val repository: MetodoPagoRepository
) {
    operator fun invoke(metodoPago: MetodoPago): Flow<Resource<MetodoPago>> = flow {
        emit(Resource.Loading())
        val result = if (metodoPago.id == 0) {
            repository.createMetodoPago(metodoPago)
        } else {
            repository.updateMetodoPago(metodoPago.id, metodoPago)
        }

        if (result is Resource.Success) {
            emit(Resource.Success(result.data!!))
        } else {
            emit(Resource.Error(result.message ?: "Error desconocido"))
        }
    }
}
