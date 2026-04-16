package edu.ucne.dulcedeleite.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetodoPagoDto(
    val id: Int,
    val nombre: String
)
