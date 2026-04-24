package edu.ucne.dulcedeleite.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.ucne.dulcedeleite.domain.model.DetallePedido
import edu.ucne.dulcedeleite.domain.model.Pedido

@Entity(tableName = "pedidos")
data class PedidoEntity(
    @PrimaryKey val id: String, // Usamos String porque generamos un UUID temporal
    val remoteId: Int? = null,
    val usuarioId: Int,
    val direccionId: Int,
    val metodoPagoId: Int,
    val fecha: String,
    val total: Double,
    val estado: String,
    val isPendingCreate: Boolean = false,
    val detallesJson: String // Guardamos los detalles como un texto JSON para simplificar Room
)

// En un proyecto real usamos Moshi o Gson para convertir la lista a JSON,
// aquí te dejo la firma de cómo se mapearían:
// fun PedidoEntity.toDomain(detalles: List<DetallePedido>) = Pedido(id, remoteId, usuarioId, direccionId, metodoPagoId, fecha, total, estado, isPendingCreate, detalles)