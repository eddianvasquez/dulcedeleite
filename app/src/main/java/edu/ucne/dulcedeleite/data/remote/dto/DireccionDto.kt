package edu.ucne.dulcedeleite.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DireccionDto(
    val id: Int,
    val usuarioId: Int,
    val calle: String,
    val ciudad: String,
    val referencia: String
)
