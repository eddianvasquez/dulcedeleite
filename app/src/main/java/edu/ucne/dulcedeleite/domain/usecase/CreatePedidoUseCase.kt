package edu.ucne.dulcedeleite.domain.usecase

import edu.ucne.dulcedeleite.data.remote.dto.PedidoDto
import edu.ucne.dulcedeleite.domain.repository.PedidoRepository
import javax.inject.Inject

class CreatePedidoUseCase @Inject constructor(
    private val repository: PedidoRepository
) {
    suspend operator fun invoke(pedidoDto: PedidoDto) = repository.crearPedido(pedidoDto)
}