package edu.ucne.dulcedeleite.data.repository

import edu.ucne.dulcedeleite.data.local.dao.DulceDeleiteDao
import edu.ucne.dulcedeleite.data.remote.dto.PedidoDto
import edu.ucne.dulcedeleite.data.remote.remotedatasource.DulceDeleiteRemoteDataSource
import edu.ucne.dulcedeleite.domain.repository.PedidoRepository
import edu.ucne.dulcedeleite.domain.utils.Resource
import javax.inject.Inject

class PedidoRepositoryImpl @Inject constructor(
    private val localDataSource: DulceDeleiteDao,
    private val remoteDataSource: DulceDeleiteRemoteDataSource
) : PedidoRepository {

    override suspend fun crearPedido(pedidoDto: PedidoDto): Resource<PedidoDto> {
        val result = remoteDataSource.createPedido(pedidoDto)
        return if (result.isSuccess) {
            Resource.Success(result.getOrNull()!!)
        } else {
            Resource.Error(result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    override suspend fun getHistorialPedidos(usuarioId: Int): Resource<List<PedidoDto>> {
        val result = remoteDataSource.getPedidosByUsuario(usuarioId)
        return if (result.isSuccess) {
            Resource.Success(result.getOrNull() ?: emptyList())
        } else {
            Resource.Error(result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }
}