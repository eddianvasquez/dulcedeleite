package edu.ucne.dulcedeleite.domain.usecase

import edu.ucne.dulcedeleite.domain.model.Pedido
import edu.ucne.dulcedeleite.domain.repository.PedidoRepository
import javax.inject.Inject

class CreatePedidoUseCase @Inject constructor(
    private val repository: PedidoRepository
) {
    suspend operator fun invoke(pedido: Pedido) = repository.createPedidoLocal(pedido)
}