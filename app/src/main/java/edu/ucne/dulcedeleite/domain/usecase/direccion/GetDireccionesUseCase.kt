package edu.ucne.dulcedeleite.domain.usecase.direccion

import edu.ucne.dulcedeleite.domain.model.Direccion
import edu.ucne.dulcedeleite.domain.repository.DireccionRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDireccionesUseCase @Inject constructor(
    private val repository: DireccionRepository
) {
    operator fun invoke(): Flow<Resource<List<Direccion>>> {
        return repository.getDirecciones()
    }
}
