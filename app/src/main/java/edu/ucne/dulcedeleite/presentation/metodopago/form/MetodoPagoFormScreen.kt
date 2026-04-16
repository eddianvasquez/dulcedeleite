package edu.ucne.dulcedeleite.presentation.metodopago.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetodoPagoFormScreen(
    id: Int,
    onNavigateBack: () -> Unit,
    viewModel: MetodoPagoFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Estados falsos para la UI visual de la tarjeta
    var numeroTarjeta by remember { mutableStateOf("") }
    var fechaExp by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    LaunchedEffect(id) {
        if (id > 0) {
            viewModel.onEvent(MetodoPagoFormUiEvent.LoadMetodoPago(id))
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        if (id > 0) "Editar Tarjeta" else "Agregar Tarjeta",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        fontSize = 18.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color(0xFF1E293B))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tarjeta Visual
            VisualCreditCard(
                nombre = uiState.nombre,
                numero = numeroTarjeta,
                vence = fechaExp
            )

            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Nombre en la tarjeta", fontWeight = FontWeight.Bold, color = Color(0xFF334155), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.onEvent(MetodoPagoFormUiEvent.NombreChanged(it)) },
                placeholder = { Text("Ej. Juan Pérez", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Número de tarjeta", fontWeight = FontWeight.Bold, color = Color(0xFF334155), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = numeroTarjeta,
                onValueChange = { if (it.length <= 16) numeroTarjeta = it },
                placeholder = { Text("0000 0000 0000 0000", color = Color.Gray) },
                trailingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
            
            // Logos falsos de tarjetas
            Row(modifier = Modifier.padding(start = 4.dp)) {
                Box(modifier = Modifier.size(width = 24.dp, height = 16.dp).background(Color(0xFF64B5F6), RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(width = 24.dp, height = 16.dp).background(Color(0xFFFFB74D), RoundedCornerShape(2.dp)))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Fecha de expiración", fontWeight = FontWeight.Bold, color = Color(0xFF334155), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = fechaExp,
                        onValueChange = { if (it.length <= 5) fechaExp = it },
                        placeholder = { Text("MM/AA", color = Color.Gray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("CVV", fontWeight = FontWeight.Bold, color = Color(0xFF334155), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 4) cvv = it },
                        placeholder = { Text("123", color = Color.Gray) },
                        trailingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.onEvent(MetodoPagoFormUiEvent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = Color.White
                ),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Guardar Tarjeta", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Sus datos de pago están protegidos con encriptación de grado bancario.",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
fun VisualCreditCard(nombre: String, numero: String, vence: String) {
    val primaryColor = MaterialTheme.colorScheme.tertiary
    val secondaryColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)

    val gradientBrush = Brush.linearGradient(
        colors = listOf(primaryColor, secondaryColor)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.586f),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
                .padding(24.dp)
        ) {
            // Icono NFC
            Icon(
                imageVector = Icons.Default.NetworkCell, 
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.align(Alignment.TopStart).size(28.dp)
            )

            // Chips falsos Top End
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                Box(modifier = Modifier.size(width = 30.dp, height = 20.dp).background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(width = 30.dp, height = 20.dp).background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp)))
            }

            // Numero centrado
            val displayNum = if (numero.isEmpty()) ".... .... .... ...." else numero.chunked(4).joinToString(" ")
            Text(
                text = displayNum,
                color = Color.White,
                fontSize = 22.sp,
                letterSpacing = 2.sp,
                modifier = Modifier.align(Alignment.CenterStart).padding(top = 16.dp)
            )

            // Nombre y Fecha Abajo
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("TITULAR", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    Text(
                        text = if (nombre.isNotBlank()) nombre.uppercase() else "NOMBRE COMPLETO",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("VENCE", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    Text(
                        text = if (vence.isNotBlank()) vence else "MM/AA",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
