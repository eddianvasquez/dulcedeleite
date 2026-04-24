package edu.ucne.dulcedeleite.domain.model

data class Direccion(
    val id: Int = 0,
    val usuarioId: Int,
    val calle: String = "",
    val ciudad: String = "",
    val referencia: String = ""
)
