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
    data object Carrito : Screen()

    @Serializable
    data object ResumenPedido : Screen()

    @Serializable
    data object MisPedidos : Screen()

    @Serializable
    data object HomeMain : Screen()

    @Serializable
    data object Nosotros : Screen()

    @Serializable
    data object Perfil : Screen()

    @Serializable
    data object DireccionesList : Screen()

    @kotlinx.serialization.Serializable
    data class DireccionForm(val id: Int) : Screen()

    @Serializable
    data object MetodosPagoList : Screen()

    @kotlinx.serialization.Serializable
    data class MetodoPagoForm(val id: Int) : Screen()
}