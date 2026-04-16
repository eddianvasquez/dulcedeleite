package edu.ucne.dulcedeleite.data.remote.remotedatasource

import edu.ucne.dulcedeleite.data.remote.api.DulceDeleiteApi

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

    suspend fun createPedido(request: PedidoDto): Result<PedidoDto> {
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

    suspend fun getPedidosByUsuario(usuarioId: Int): Result<List<PedidoDto>> {
        return try {
            val response = api.getPedidos()
            if (response.isSuccessful) {
                val allPedidos = response.body() ?: emptyList()
                val filtered = allPedidos.filter { it.usuarioId == usuarioId }
                Result.success(filtered)
            } else {
                Result.failure(Exception("Error de red ${response.code()}"))
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

    // --- DIRECCIONES ---
    suspend fun getDirecciones(): Result<List<edu.ucne.dulcedeleite.data.remote.dto.DireccionDto>> {
        return try {
            val response = api.getDirecciones()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error de red ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun getDireccion(id: Int): Result<edu.ucne.dulcedeleite.data.remote.dto.DireccionDto> {
        return try {
            val response = api.getDireccion(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error de red ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun createDireccion(request: edu.ucne.dulcedeleite.data.remote.dto.DireccionDto): Result<edu.ucne.dulcedeleite.data.remote.dto.DireccionDto> {
        return try {
            val response = api.createDireccion(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun updateDireccion(id: Int, request: edu.ucne.dulcedeleite.data.remote.dto.DireccionDto): Result<edu.ucne.dulcedeleite.data.remote.dto.DireccionDto> {
        return try {
            val response = api.updateDireccion(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun deleteDireccion(id: Int): Result<Unit> {
        return try {
            val response = api.deleteDireccion(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    // --- METODOS DE PAGO ---
    suspend fun getMetodosPago(): Result<List<edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto>> {
        return try {
            val response = api.getMetodosPago()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error de red ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun getMetodoPago(id: Int): Result<edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto> {
        return try {
            val response = api.getMetodoPago(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error de red ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun createMetodoPago(request: edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto): Result<edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto> {
        return try {
            val response = api.createMetodoPago(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun updateMetodoPago(id: Int, request: edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto): Result<edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto> {
        return try {
            val response = api.updateMetodoPago(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }

    suspend fun deleteMetodoPago(id: Int): Result<Unit> {
        return try {
            val response = api.deleteMetodoPago(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Error de red"))
        }
    }
}