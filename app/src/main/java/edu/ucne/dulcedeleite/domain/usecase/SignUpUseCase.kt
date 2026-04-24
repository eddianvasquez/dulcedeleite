package edu.ucne.dulcedeleite.domain.usecase

import edu.ucne.dulcedeleite.data.remote.dto.RegistroResponseDto
import edu.ucne.dulcedeleite.data.remote.dto.RegistroUsuarioDto
import edu.ucne.dulcedeleite.domain.repository.AuthRepository
import edu.ucne.dulcedeleite.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(registroUsuarioDto: RegistroUsuarioDto): Flow<Resource<RegistroResponseDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.registro(registroUsuarioDto)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error de Registro: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Excepción al intentar registrar: ${e.message}"))
        }
    }
}
