package edu.ucne.dulcedeleite.domain.model

import java.util.UUID

data class Pedido(
    val id: String = UUID.randomUUID().toString(), // ID local para Offline-First
    val remoteId: Int? = null, // ID de la API en Somee
    val usuarioId: Int,
    val direccionId: Int,
    val metodoPagoId: Int,
    val fecha: String,
    val total: Double,
    val estado: String,
    val isPendingCreate: Boolean = false, // Bandera Offline-First
    val detalles: List<DetallePedido>
)

data class DetallePedido(
    val id: Int = 0,
    val pedidoId: Int = 0,
    val productoId: Int,
    val cantidad: Int,
    val precioUnitario: Double
)