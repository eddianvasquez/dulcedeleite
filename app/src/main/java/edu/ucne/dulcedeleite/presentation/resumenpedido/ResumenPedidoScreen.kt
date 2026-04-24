package edu.ucne.dulcedeleite.presentation.resumenpedido

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import edu.ucne.dulcedeleite.data.remote.dto.DireccionDto
import edu.ucne.dulcedeleite.data.remote.dto.MetodoPagoDto
import edu.ucne.dulcedeleite.presentation.carrito.CartItem
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenPedidoScreen(
    viewModel: ResumenPedidoViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onCheckoutSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.isPostSuccess) {
        if (uiState.isPostSuccess) {
            onCheckoutSuccess()
        }
    }

    if (uiState.showDireccionSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(ResumenPedidoUiEvent.ToggleDireccionSheet(false)) },
            containerColor = Color(0xFF1E1E1E)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text("Selecciona una Dirección", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(uiState.direcciones) { direccion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.onEvent(ResumenPedidoUiEvent.SelectDireccion(direccion)) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.selectedDireccion?.id == direccion.id,
                            onClick = { viewModel.onEvent(ResumenPedidoUiEvent.SelectDireccion(direccion)) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF8B5A2B), unselectedColor = Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = direccion.calle, color = Color.White)
                            Text(text = "${direccion.ciudad} - ${direccion.referencia}", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    if (uiState.showMetodoPagoSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(ResumenPedidoUiEvent.ToggleMetodoPagoSheet(false)) },
            containerColor = Color(0xFF1E1E1E)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text("Selecciona Método de Pago", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(uiState.metodosPago) { metodo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.onEvent(ResumenPedidoUiEvent.SelectMetodoPago(metodo)) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.selectedMetodoPago?.id == metodo.id,
                            onClick = { viewModel.onEvent(ResumenPedidoUiEvent.SelectMetodoPago(metodo)) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF8B5A2B), unselectedColor = Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = metodo.nombre, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar Compra", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        containerColor = Color(0xFF1E1E1E)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading || uiState.isLoadingData) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF8B5A2B))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        Text(text = "RESUMEN DE TU PEDIDO", color = Color(0xFF8B5A2B), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Productos limitados para resumen
                    items(uiState.cartItems) { item ->
                        ItemCartSummary(item)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Mensaje Personalizado", color = Color(0xFF8B5A2B), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.mensajePersonalizado,
                            onValueChange = { viewModel.onEvent(ResumenPedidoUiEvent.UpdateMensaje(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(Color.White, RoundedCornerShape(12.dp)),
                            placeholder = { Text("Escribe un mensaje para tu pastel...") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF8B5A2B),
                                unfocusedBorderColor = Color.Transparent,
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Fecha de Entrega", color = Color(0xFF8B5A2B), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .background(Color.White, RoundedCornerShape(8.dp))
                                        .clickable {
                                            val c = Calendar.getInstance()
                                            DatePickerDialog(context, { _, year, month, dayOfMonth ->
                                                viewModel.onEvent(ResumenPedidoUiEvent.UpdateFecha(LocalDate.of(year, month + 1, dayOfMonth)))
                                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
                                        }
                                        .padding(horizontal = 12.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = uiState.fechaEntrega?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "Seleccionar",
                                        color = Color.Black
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Hora de Entrega", color = Color(0xFF8B5A2B), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .background(Color.White, RoundedCornerShape(8.dp))
                                        .clickable {
                                            val c = Calendar.getInstance()
                                            TimePickerDialog(context, { _, hourOfDay, minute ->
                                                viewModel.onEvent(ResumenPedidoUiEvent.UpdateHora(LocalTime.of(hourOfDay, minute)))
                                            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show()
                                        }
                                        .padding(horizontal = 12.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = uiState.horaEntrega?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "Seleccionar",
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    item {
                        Text(text = "ENTREGA Y PAGO", color = Color(0xFF8B5A2B), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Delivery Card
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.onEvent(ResumenPedidoUiEvent.ToggleDireccionSheet(true)) },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = "Ubicación", tint = Color(0xFF8B5A2B))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "DIRECCIÓN DE ENVÍO", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    val dir = uiState.selectedDireccion
                                    val dirText = if (dir != null) "${dir.calle}, ${dir.ciudad}, ${dir.referencia}" else "No seleccionada"
                                    Text(text = dirText, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                }
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.LightGray)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))

                        // Payment Card
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.onEvent(ResumenPedidoUiEvent.ToggleMetodoPagoSheet(true)) },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Add your own icon here or use a placeholder that means payment
                                Icon(Icons.Default.DateRange, contentDescription = "Pago", tint = Color(0xFF8B5A2B))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "MÉTODO DE PAGO", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    val method = uiState.selectedMetodoPago
                                    val methodText = method?.nombre ?: "No seleccionado"
                                    Text(text = methodText, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                                }
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.LightGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { viewModel.onEvent(ResumenPedidoUiEvent.ConfirmarPedido) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8B5A2B),
                                disabledContainerColor = Color.Gray
                            ),
                            enabled = uiState.selectedDireccion != null && uiState.selectedMetodoPago != null && uiState.cartItems.isNotEmpty()
                        ) {
                            Text("Confirmar y Pagar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "* Al confirmar, se creará su pedido y podrá ver su historial en 'Pedidos'.",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCartSummary(item: CartItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9F5)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.producto.imagenUrl,
                contentDescription = item.producto.nombre,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = item.producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E2632))
                Text(text = "Cantidad: ${item.cantidad}", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "$${item.producto.precio * item.cantidad}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF8B5A2B),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
