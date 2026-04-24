package edu.ucne.dulcedeleite.presentation.proyecto.edit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoEditScreen(
    viewModel: ProductoEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.onEvent(ProductoEditUiEvent.OnImagenUrlChanged(uri.toString()))
            }
        }
    )

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.id == 0) "Crear Producto" else "Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(ProductoEditUiEvent.OnSave) }) {
                Icon(Icons.Default.Check, contentDescription = "Guardar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            uiState.errorMessage?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.onEvent(ProductoEditUiEvent.OnNombreChanged(it)) },
                label = { Text("Nombre") },
                isError = uiState.nombreError != null,
                supportingText = { uiState.nombreError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.onEvent(ProductoEditUiEvent.OnDescripcionChanged(it)) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.precio,
                onValueChange = { viewModel.onEvent(ProductoEditUiEvent.OnPrecioChanged(it)) },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.precioError != null,
                supportingText = { uiState.precioError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.stock,
                onValueChange = { viewModel.onEvent(ProductoEditUiEvent.OnStockChanged(it)) },
                label = { Text("Stock") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.stockError != null,
                supportingText = { uiState.stockError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { 
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Seleccionar Imagen")
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (uiState.imagenUrl.isNotBlank()) "Cambiar Imagen" else "Seleccionar Imagen desde Galería")
            }

            if (uiState.imagenUrl.isNotBlank()) {
                Text("Previsualización de imagen:", style = MaterialTheme.typography.bodySmall)
                AsyncImage(
                    model = uiState.imagenUrl,
                    contentDescription = "Previsualización",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}
