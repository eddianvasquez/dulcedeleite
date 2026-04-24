package edu.ucne.dulcedeleite.data.remote.api

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

    // --- PEDIDOS ---
    @POST("api/Pedidos")
    suspend fun createPedido(@Body request: PedidoDto): Response<PedidoDto>

    @GET("api/Pedidos")
    suspend fun getPedidos(): Response<List<PedidoDto>>

    // --- DIRECCIONES ---
    @GET("api/Direcciones")
    suspend fun getDirecciones(): Response<List<edu.ucne.dulcedeleite.data.remote.dto.DireccionDto>>

    @GET("api/Direcciones/{id}")
    suspend fun getDireccion(@retrofit2.http.Path("id") id: Int): Response<edu.ucne.dulcedeleite.data.remote.dto.DireccionDto>

    @POST("api/Direcciones")
    suspend fun createDireccion(@Body request: edu.ucne.dulcedeleite.data.remote.dto.DireccionDto): Response<edu.ucne.dulcedeleite.data.remote.dto.DireccionDto>

    @PUT("api/Direcciones")
    suspend fun updateDireccion(@Body request: edu.ucne.dulcedeleite.data.remote.dto.DireccionDto): Response<edu.ucne.dulcedeleite.data.remote.dto.DireccionDto>

    @DELETE("api/Direcciones/{id}")
    suspend fun deleteDireccion(@retrofit2.http.Path("id") id: Int): Response<Unit>

    // --- METODOS DE PAGO ---
    @GET("api/MetodosPago")
    suspend fun getMetodosPago(): Response<List<edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto>>

    @GET("api/MetodosPago/{id}")
    suspend fun getMetodoPago(@retrofit2.http.Path("id") id: Int): Response<edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto>

    @POST("api/MetodosPago")
    suspend fun createMetodoPago(@Body request: edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto): Response<edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto>

    @PUT("api/MetodosPago")
    suspend fun updateMetodoPago(@Body request: edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto): Response<edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto>

    @DELETE("api/MetodosPago/{id}")
    suspend fun deleteMetodoPago(@retrofit2.http.Path("id") id: Int): Response<Unit>
}