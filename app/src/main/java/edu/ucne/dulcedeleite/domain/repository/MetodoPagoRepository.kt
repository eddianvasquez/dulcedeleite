package edu.ucne.dulcedeleite.domain.repository

import edu.ucne.dulcedeleite.domain.model.MetodoPago
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MetodoPagoRepository {
    fun getMetodosPago(): Flow<Resource<List<MetodoPago>>>
    fun getMetodoPago(id: Int): Flow<Resource<MetodoPago>>
    suspend fun createMetodoPago(metodoPago: MetodoPago): Resource<MetodoPago>
    suspend fun updateMetodoPago(id: Int, metodoPago: MetodoPago): Resource<MetodoPago>
    suspend fun deleteMetodoPago(id: Int): Resource<Unit>
}
