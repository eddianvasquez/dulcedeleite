package edu.ucne.dulcedeleite.data.remote

import edu.ucne.dulcedeleite.data.remote.dto.LoginDto
import edu.ucne.dulcedeleite.data.remote.dto.RegistroUsuarioDto
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi
) {
    suspend fun login(loginDto: LoginDto) = authApi.login(loginDto)
    suspend fun registro(registroUsuarioDto: RegistroUsuarioDto) = authApi.registro(registroUsuarioDto)
}
