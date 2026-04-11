package edu.ucne.dulcedeleite.data.remote.api

import edu.ucne.dulcedeleite.data.remote.dto.CreatePedidoDto
import edu.ucne.dulcedeleite.data.remote.dto.PedidoDto
import edu.ucne.dulcedeleite.data.remote.dto.ProductoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DulceDeleiteApi {

    // --- PRODUCTOS ---
    @GET("api/Productos")
    suspend fun getProductos(): Response<List<ProductoDto>>

    // --- PEDIDOS (Para el Carrito Offline-First) ---
    @POST("api/Pedidos")
    suspend fun createPedido(@Body request: CreatePedidoDto): Response<PedidoDto>
}