package edu.ucne.dulcedeleite.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistroResponseDto(
    val mensaje: String
)
