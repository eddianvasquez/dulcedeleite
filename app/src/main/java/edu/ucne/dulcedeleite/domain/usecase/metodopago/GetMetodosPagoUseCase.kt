package edu.ucne.dulcedeleite.domain.usecase.metodopago

import edu.ucne.dulcedeleite.domain.model.MetodoPago
import edu.ucne.dulcedeleite.domain.repository.MetodoPagoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMetodosPagoUseCase @Inject constructor(
    private val repository: MetodoPagoRepository
) {
    operator fun invoke(): Flow<Resource<List<MetodoPago>>> {
        return repository.getMetodosPago()
    }
}
