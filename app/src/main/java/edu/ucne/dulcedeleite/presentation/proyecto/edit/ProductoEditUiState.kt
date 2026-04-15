package edu.ucne.dulcedeleite.presentation.proyecto.edit

data class ProductoEditUiState(
    val id: Int = 0,
    val nombre: String = "",
    val nombreError: String? = null,
    val descripcion: String = "",
    val descripcionError: String? = null,
    val precio: String = "",
    val precioError: String? = null,
    val stock: String = "",
    val stockError: String? = null,
    val imagenUrl: String = "",
    val imagenUrlError: String? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
