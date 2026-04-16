package edu.ucne.dulcedeleite.domain.usecase.pedido

import edu.ucne.dulcedeleite.data.remote.dto.PedidoDto
import edu.ucne.dulcedeleite.domain.repository.PedidoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CrearPedidoUseCase @Inject constructor(
    private val repository: PedidoRepository
) {
    operator fun invoke(pedidoDto: PedidoDto): Flow<Resource<PedidoDto>> = flow {
        emit(Resource.Loading())
        try {
            val result = repository.crearPedido(pedidoDto)
            emit(result)
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
        }
    }
}
