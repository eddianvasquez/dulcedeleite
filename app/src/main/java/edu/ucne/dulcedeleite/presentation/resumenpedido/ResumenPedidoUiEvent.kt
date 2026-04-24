package edu.ucne.dulcedeleite.presentation.resumenpedido

import edu.ucne.dulcedeleite.data.remote.dto.DireccionDto
import edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto
import java.time.LocalDate
import java.time.LocalTime

sealed interface ResumenPedidoUiEvent {
    object ConfirmarPedido : ResumenPedidoUiEvent
    data class ToggleDireccionSheet(val show: Boolean) : ResumenPedidoUiEvent
    data class ToggleMetodoPagoSheet(val show: Boolean) : ResumenPedidoUiEvent
    data class SelectDireccion(val direccion: DireccionDto) : ResumenPedidoUiEvent
    data class SelectMetodoPago(val metodoPago: MetodoPagoDto) : ResumenPedidoUiEvent
    data class UpdateMensaje(val mensaje: String) : ResumenPedidoUiEvent
    data class UpdateFecha(val fecha: LocalDate) : ResumenPedidoUiEvent
    data class UpdateHora(val hora: LocalTime) : ResumenPedidoUiEvent
}
