package edu.ucne.dulcedeleite.domain.usecase

import edu.ucne.dulcedeleite.data.remote.dto.LoginDto
import edu.ucne.dulcedeleite.data.remote.dto.LoginResponseDto
import edu.ucne.dulcedeleite.domain.repository.AuthRepository
import edu.ucne.dulcedeleite.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(loginDto: LoginDto): Flow<Resource<LoginResponseDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.login(loginDto)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error de Login: ${response.message()} o credenciales incorrectas"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Excepción al intentar iniciar sesión: ${e.message}"))
        }
    }
}
