package edu.ucne.dulcedeleite.domain.repository

import edu.ucne.dulcedeleite.domain.model.Direccion
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface DireccionRepository {
    fun getDirecciones(): Flow<Resource<List<Direccion>>>
    fun getDireccion(id: Int): Flow<Resource<Direccion>>
    suspend fun createDireccion(direccion: Direccion): Resource<Direccion>
    suspend fun updateDireccion(id: Int, direccion: Direccion): Resource<Direccion>
    suspend fun deleteDireccion(id: Int): Resource<Unit>
}
