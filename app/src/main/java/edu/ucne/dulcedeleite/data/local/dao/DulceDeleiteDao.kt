package edu.ucne.dulcedeleite.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.dulcedeleite.data.local.entity.PedidoEntity
import edu.ucne.dulcedeleite.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DulceDeleiteDao {
    // --- PRODUCTOS ---
    @Query("SELECT * FROM productos")
    fun observeProductos(): Flow<List<ProductoEntity>>

    @Upsert
    suspend fun upsertProductos(productos: List<ProductoEntity>)

    @Query("DELETE FROM productos")
    suspend fun clearProductos()

    // --- PEDIDOS (Offline-First) ---
    @Upsert
    suspend fun upsertPedido(pedido: PedidoEntity)

    @Query("SELECT * FROM pedidos WHERE isPendingCreate = 1")
    suspend fun getPendingCreatePedidos(): List<PedidoEntity>
}