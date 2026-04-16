package edu.ucne.dulcedeleite.domain.repository

import edu.ucne.dulcedeleite.data.remote.dto.PedidoDto
import edu.ucne.dulcedeleite.domain.utils.Resource

interface PedidoRepository {
    suspend fun crearPedido(pedidoDto: PedidoDto): Resource<PedidoDto>
    suspend fun getHistorialPedidos(usuarioId: Int): Resource<List<PedidoDto>>
}