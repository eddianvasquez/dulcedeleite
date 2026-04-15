package edu.ucne.dulcedeleite.data.remote.api

import edu.ucne.dulcedeleite.data.remote.dto.CreatePedidoDto
import edu.ucne.dulcedeleite.data.remote.dto.PedidoDto
import edu.ucne.dulcedeleite.data.remote.dto.ProductoDto
import edu.ucne.dulcedeleite.data.remote.dto.UploadResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface DulceDeleiteApi {

    // --- PRODUCTOS ---
    @GET("api/Productos")
    suspend fun getProductos(): Response<List<ProductoDto>>

    @POST("api/Productos")
    suspend fun createProducto(@Body request: ProductoDto): Response<ProductoDto>

    @PUT("api/Productos/{id}")
    suspend fun updateProducto(@retrofit2.http.Path("id") id: Int, @Body request: ProductoDto): Response<ProductoDto>

    @DELETE("api/Productos/{id}")
    suspend fun deleteProducto(@retrofit2.http.Path("id") id: Int): Response<Unit>

    // --- UPLOADS ---
    @Multipart
    @POST("api/Upload")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<UploadResponseDto>

    // --- PEDIDOS (Para el Carrito Offline-First) ---
    @POST("api/Pedidos")
    suspend fun createPedido(@Body request: CreatePedidoDto): Response<PedidoDto>
}