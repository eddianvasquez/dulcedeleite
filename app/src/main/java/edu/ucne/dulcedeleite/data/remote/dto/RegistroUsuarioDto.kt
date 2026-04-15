package edu.ucne.dulcedeleite.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistroUsuarioDto(
    val nombre: String,
    val email: String,
    val password: String,
    val telefono: String
)
