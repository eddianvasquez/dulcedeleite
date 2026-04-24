package edu.ucne.dulcedeleite.data.remote.dto

import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class LoginDto(
    val email: String,
    val password: String
)
