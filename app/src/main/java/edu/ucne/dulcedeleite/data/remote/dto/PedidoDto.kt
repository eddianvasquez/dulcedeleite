package edu.ucne.dulcedeleite.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PedidoDto(
    val id: Int,
    val usuarioId: Int,
    val direccionId: Int,
    val metodoPagoId: Int,
    val fecha: String,
    val total: Double,
    val estado: String,
    val detalles: List<DetallePedidoDto>
)

@JsonClass(generateAdapter = true)
data class DetallePedidoDto(
    val id: Int,
    val pedidoId: Int,
    val productoId: Int,
    val cantidad: Int,
    val precioUnitario: Double
)