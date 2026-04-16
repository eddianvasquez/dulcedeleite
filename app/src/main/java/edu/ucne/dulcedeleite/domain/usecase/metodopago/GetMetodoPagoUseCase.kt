package edu.ucne.dulcedeleite.domain.usecase.metodopago

import edu.ucne.dulcedeleite.domain.model.MetodoPago
import edu.ucne.dulcedeleite.domain.repository.MetodoPagoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMetodoPagoUseCase @Inject constructor(
    private val repository: MetodoPagoRepository
) {
    operator fun invoke(id: Int): Flow<Resource<MetodoPago>> {
        return repository.getMetodoPago(id)
    }
}
