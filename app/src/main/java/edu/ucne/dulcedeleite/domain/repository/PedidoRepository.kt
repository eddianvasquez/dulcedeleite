package edu.ucne.dulcedeleite.domain.repository

import edu.ucne.dulcedeleite.domain.model.Pedido
import edu.ucne.dulcedeleite.domain.utils.Resource

interface PedidoRepository {
    // Lógica Offline-First exacta de la documentación
    suspend fun createPedidoLocal(pedido: Pedido): Resource<Pedido>
    suspend fun postPendingPedidos(): Resource<Unit>
}