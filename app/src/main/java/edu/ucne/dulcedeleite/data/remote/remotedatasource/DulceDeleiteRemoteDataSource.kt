package edu.ucne.dulcedeleite.data.remote.remotedatasource

import edu.ucne.dulcedeleite.data.remote.api.DulceDeleiteApi
import edu.ucne.dulcedeleite.data.remote.dto.CreatePedidoDto
import edu.ucne.dulcedeleite.data.remote.dto.PedidoDto
import edu.ucne.dulcedeleite.data.remote.dto.ProductoDto
import retrofit2.HttpException
import javax.inject.Inject

class DulceDeleiteRemoteDataSource @Inject constructor(
    private val api: DulceDeleiteApi
) {
    suspend fun getProductos(): Result<List<ProductoDto>> {
        return try {
            val response = api.getProductos()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error de red ${response.code()}"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("Error de servidor", e))
        } catch (e: Exception) {
            Result.failure(Exception("Error desconocido", e))
        }
    }

    suspend fun createProducto(request: ProductoDto): Result<ProductoDto> {
        return try {
            val response = api.createProducto(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun updateProducto(id: Int, request: ProductoDto): Result<ProductoDto> {
        return try {
            val response = api.updateProducto(id, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun deleteProducto(id: Int): Result<Unit> {
        return try {
            val response = api.deleteProducto(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun createPedido(request: CreatePedidoDto): Result<PedidoDto> {
        return try {
            val response = api.createPedido(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun uploadImage(file: okhttp3.MultipartBody.Part): Result<edu.ucne.dulcedeleite.data.remote.dto.UploadResponseDto> {
        return try {
            val response = api.uploadImage(file)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error subiendo imagen"))
        }
    }
}