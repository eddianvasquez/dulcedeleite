package edu.ucne.dulcedeleite.presentation.proyecto.list

import edu.ucne.dulcedeleite.domain.model.Producto

data class ProductoListUiState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val errorMessage: String? = null
)
