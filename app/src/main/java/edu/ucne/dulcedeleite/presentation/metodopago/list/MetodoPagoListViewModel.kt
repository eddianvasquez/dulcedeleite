package edu.ucne.dulcedeleite.presentation.metodopago.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.domain.usecase.metodopago.DeleteMetodoPagoUseCase
import edu.ucne.dulcedeleite.domain.usecase.metodopago.GetMetodosPagoUseCase
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MetodoPagoListViewModel @Inject constructor(
    private val getMetodosPagoUseCase: GetMetodosPagoUseCase,
    private val deleteMetodoPagoUseCase: DeleteMetodoPagoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MetodoPagoListUiState())
    val uiState: StateFlow<MetodoPagoListUiState> = _uiState.asStateFlow()

    init {
        loadMetodosPago()
    }

    fun onEvent(event: MetodoPagoListUiEvent) {
        when (event) {
            is MetodoPagoListUiEvent.DeleteMetodoPago -> {
                deleteMetodoPago(event.id)
            }
            MetodoPagoListUiEvent.RefreshDefault -> {
                loadMetodosPago()
            }
        }
    }

    private fun loadMetodosPago() {
        getMetodosPagoUseCase().onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                is Resource.Success -> {
                    _uiState.update { 
                        it.copy(isLoading = false, metodosPago = result.data ?: emptyList()) 
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteMetodoPago(id: Int) {
        deleteMetodoPagoUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    loadMetodosPago()
                }
            }
        }.launchIn(viewModelScope)
    }
}
