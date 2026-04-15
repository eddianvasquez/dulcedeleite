package edu.ucne.dulcedeleite.data.remote

import edu.ucne.dulcedeleite.data.remote.dto.LoginDto
import edu.ucne.dulcedeleite.data.remote.dto.LoginResponseDto
import edu.ucne.dulcedeleite.data.remote.dto.RegistroResponseDto
import edu.ucne.dulcedeleite.data.remote.dto.RegistroUsuarioDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/Usuarios/login")
    suspend fun login(@Body loginDto: LoginDto): Response<LoginResponseDto>

    @POST("api/Usuarios/registro")
    suspend fun registro(@Body registroUsuarioDto: RegistroUsuarioDto): Response<RegistroResponseDto>
}
