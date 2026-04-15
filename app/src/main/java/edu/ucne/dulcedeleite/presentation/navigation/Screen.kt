package edu.ucne.dulcedeleite.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object Login : Screen()

    @Serializable
    data object SignUp : Screen()

    @Serializable
    data object ProductoList : Screen()

    @kotlinx.serialization.Serializable
    data class ProductoDetail(val id: Int) : Screen()

    @kotlinx.serialization.Serializable
    data class ProductoEdit(val id: Int) : Screen()

    @Serializable
    data object Dashboard : Screen()

    @Serializable
    data object Pedidos : Screen()

    @Serializable
    data object HomeMain : Screen()

    @Serializable
    data object Nosotros : Screen()

    @Serializable
    data object Perfil : Screen()
}