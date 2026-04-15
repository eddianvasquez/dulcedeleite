package edu.ucne.dulcedeleite.data.repository

import edu.ucne.dulcedeleite.data.remote.AuthRemoteDataSource
import edu.ucne.dulcedeleite.data.remote.dto.LoginDto
import edu.ucne.dulcedeleite.data.remote.dto.RegistroUsuarioDto
import edu.ucne.dulcedeleite.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun login(loginDto: LoginDto) = authRemoteDataSource.login(loginDto)
    
    override suspend fun registro(registroUsuarioDto: RegistroUsuarioDto) = authRemoteDataSource.registro(registroUsuarioDto)
}
