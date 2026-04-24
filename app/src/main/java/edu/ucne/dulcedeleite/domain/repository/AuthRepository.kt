package edu.ucne.dulcedeleite.domain.repository

import edu.ucne.dulcedeleite.data.remote.dto.LoginDto
import edu.ucne.dulcedeleite.data.remote.dto.LoginResponseDto
import edu.ucne.dulcedeleite.data.remote.dto.RegistroResponseDto
import edu.ucne.dulcedeleite.data.remote.dto.RegistroUsuarioDto
import retrofit2.Response

interface AuthRepository {
    suspend fun login(loginDto: LoginDto): Response<LoginResponseDto>
    suspend fun registro(registroUsuarioDto: RegistroUsuarioDto): Response<RegistroResponseDto>
}
