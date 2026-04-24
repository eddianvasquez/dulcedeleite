package edu.ucne.dulcedeleite.data.remote.dto

import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class LoginResponseDto(
    val token: String,
    val usuarioId: Int,
    val nombre: String,
    val rol: String
)
