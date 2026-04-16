package edu.ucne.dulcedeleite.presentation.metodopago.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.domain.model.MetodoPago
import edu.ucne.dulcedeleite.domain.usecase.metodopago.GetMetodoPagoUseCase
import edu.ucne.dulcedeleite.domain.usecase.metodopago.SaveMetodoPagoUseCase
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MetodoPagoFormViewModel @Inject constructor(
    private val saveMetodoPagoUseCase: SaveMetodoPagoUseCase,
    private val getMetodoPagoUseCase: GetMetodoPagoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MetodoPagoFormUiState())
    val uiState: StateFlow<MetodoPagoFormUiState> = _uiState.asStateFlow()

    fun onEvent(event: MetodoPagoFormUiEvent) {
        when (event) {
            is MetodoPagoFormUiEvent.NombreChanged -> {
                _uiState.update { it.copy(nombre = event.nombre) }
            }
            is MetodoPagoFormUiEvent.LoadMetodoPago -> {
                if (event.id > 0) {
                    loadMetodoPago(event.id)
                }
            }
            MetodoPagoFormUiEvent.Submit -> {
                submitMetodoPago()
            }
        }
    }

    private fun loadMetodoPago(id: Int) {
        getMetodoPagoUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    val d = result.data
                    if (d != null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                id = d.id,
                                nombre = d.nombre
                            )
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun submitMetodoPago() {
        val state = _uiState.value

        if (state.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido.") }
            return
        }

        saveMetodoPagoUseCase(
            MetodoPago(
                id = state.id,
                nombre = state.nombre
            )
        ).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
            }
        }.launchIn(viewModelScope)
    }
}
