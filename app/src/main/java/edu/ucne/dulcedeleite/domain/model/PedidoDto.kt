package edu.ucne.dulcedeleite.data.remote.dto

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

data class DetallePedidoDto(
    val id: Int,
    val pedidoId: Int,
    val productoId: Int,
    val cantidad: Int,
    val precioUnitario: Double
)

// DTO para cuando enviamos un pedido nuevo (No necesita ID ni Fecha, la API lo genera)
data class CreatePedidoDto(
    val usuarioId: Int,
    val direccionId: Int,
    val metodoPagoId: Int,
    val detalles: List<CreateDetallePedidoDto>
)

data class CreateDetallePedidoDto(
    val productoId: Int,
    val cantidad: Int,
    val precioUnitario: Double
)