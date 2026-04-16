package edu.ucne.dulcedeleite.domain.usecase.direccion

import edu.ucne.dulcedeleite.domain.model.Direccion
import edu.ucne.dulcedeleite.domain.repository.DireccionRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDireccionUseCase @Inject constructor(
    private val repository: DireccionRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Direccion>> {
        return repository.getDireccion(id)
    }
}
