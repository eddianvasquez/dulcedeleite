package edu.ucne.dulcedeleite.data.repository

import edu.ucne.dulcedeleite.data.local.dao.DulceDeleiteDao
import edu.ucne.dulcedeleite.data.local.entity.PedidoEntity
import edu.ucne.dulcedeleite.data.remote.dto.CreateDetallePedidoDto
import edu.ucne.dulcedeleite.data.remote.dto.CreatePedidoDto
import edu.ucne.dulcedeleite.data.remote.remotedatasource.DulceDeleiteRemoteDataSource
import edu.ucne.dulcedeleite.domain.model.Pedido
import edu.ucne.dulcedeleite.domain.repository.PedidoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import javax.inject.Inject

class PedidoRepositoryImpl @Inject constructor(
    private val localDataSource: DulceDeleiteDao,
    private val remoteDataSource: DulceDeleiteRemoteDataSource
) : PedidoRepository {

    // 1. Guardar primero en el celular
    override suspend fun createPedidoLocal(pedido: Pedido): Resource<Pedido> {
        val pending = pedido.copy(isPendingCreate = true)

        val entity = PedidoEntity(
            id = pending.id,
            usuarioId = pending.usuarioId,
            direccionId = pending.direccionId,
            metodoPagoId = pending.metodoPagoId,
            fecha = pending.fecha,
            total = pending.total,
            estado = pending.estado,
            isPendingCreate = pending.isPendingCreate,
            detallesJson = "[]" // Aquí iría la conversión a JSON
        )
        localDataSource.upsertPedido(entity)
        return Resource.Success(pending)
    }

    // 2. Enviar a Somee cuando haya internet (WorkManager llama a esto)
    override suspend fun postPendingPedidos(): Resource<Unit> {
        val pendingList = localDataSource.getPendingCreatePedidos()

        for (pedidoEntity in pendingList) {
            val request = CreatePedidoDto(
                usuarioId = pedidoEntity.usuarioId,
                direccionId = pedidoEntity.direccionId,
                metodoPagoId = pedidoEntity.metodoPagoId,
                detalles = emptyList() // Aquí se decodifica el JSON de detalles
            )

            val result = remoteDataSource.createPedido(request)
            if (result.isSuccess) {
                // Si la API responde bien, quitamos la bandera de pendiente y le ponemos el ID real
                val synced = pedidoEntity.copy(
                    remoteId = result.getOrNull()?.id,
                    isPendingCreate = false
                )
                localDataSource.upsertPedido(synced)
            } else {
                return Resource.Error("Falló sincronización")
            }
        }
        return Resource.Success(Unit)
    }
}