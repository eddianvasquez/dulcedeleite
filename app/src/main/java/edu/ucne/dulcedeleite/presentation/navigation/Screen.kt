package edu.ucne.dulcedeleite.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object ProductoList : Screen()

    @Serializable
    data class ProductoDetail(val id: Int) : Screen()
}