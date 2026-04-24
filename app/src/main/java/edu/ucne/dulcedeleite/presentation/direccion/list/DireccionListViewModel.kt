package edu.ucne.dulcedeleite.presentation.direccion.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.domain.usecase.direccion.DeleteDireccionUseCase
import edu.ucne.dulcedeleite.domain.usecase.direccion.GetDireccionesUseCase
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DireccionListViewModel @Inject constructor(
    private val getDireccionesUseCase: GetDireccionesUseCase,
    private val deleteDireccionUseCase: DeleteDireccionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DireccionListUiState())
    val uiState: StateFlow<DireccionListUiState> = _uiState.asStateFlow()

    init {
        loadDirecciones()
    }

    fun onEvent(event: DireccionListUiEvent) {
        when (event) {
            is DireccionListUiEvent.DeleteDireccion -> {
                deleteDireccion(event.id)
            }
            DireccionListUiEvent.RefreshDefault -> {
                loadDirecciones()
            }
        }
    }

    private fun loadDirecciones() {
        getDireccionesUseCase().onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                is Resource.Success -> {
                    _uiState.update { 
                        it.copy(isLoading = false, direcciones = result.data ?: emptyList()) 
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteDireccion(id: Int) {
        deleteDireccionUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    loadDirecciones() // Recargar la lista
                }
            }
        }.launchIn(viewModelScope)
    }
}
