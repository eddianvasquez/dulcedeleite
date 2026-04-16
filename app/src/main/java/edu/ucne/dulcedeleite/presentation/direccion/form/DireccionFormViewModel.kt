package edu.ucne.dulcedeleite.presentation.direccion.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.data.local.datastore.AuthTokenManager
import edu.ucne.dulcedeleite.domain.model.Direccion
import edu.ucne.dulcedeleite.domain.usecase.direccion.GetDireccionUseCase
import edu.ucne.dulcedeleite.domain.usecase.direccion.SaveDireccionUseCase
import edu.ucne.dulcedeleite.domain.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DireccionFormViewModel @Inject constructor(
    private val saveDireccionUseCase: SaveDireccionUseCase,
    private val getDireccionUseCase: GetDireccionUseCase,
    private val tokenManager: AuthTokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DireccionFormUiState())
    val uiState: StateFlow<DireccionFormUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val usrId = tokenManager.getUsuarioId().firstOrNull() ?: 0
            _uiState.update { it.copy(usuarioId = usrId) }
        }
    }

    fun onEvent(event: DireccionFormUiEvent) {
        when (event) {
            is DireccionFormUiEvent.CalleChanged -> {
                _uiState.update { it.copy(calle = event.calle) }
            }
            is DireccionFormUiEvent.CiudadChanged -> {
                _uiState.update { it.copy(ciudad = event.ciudad) }
            }
            is DireccionFormUiEvent.ReferenciaChanged -> {
                _uiState.update { it.copy(referencia = event.referencia) }
            }
            is DireccionFormUiEvent.LoadDireccion -> {
                if (event.id > 0) {
                    loadDireccion(event.id)
                }
            }
            DireccionFormUiEvent.Submit -> {
                submitDireccion()
            }
        }
    }

    private fun loadDireccion(id: Int) {
        getDireccionUseCase(id).onEach { result ->
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
                                usuarioId = d.usuarioId,
                                calle = d.calle,
                                ciudad = d.ciudad,
                                referencia = d.referencia
                            )
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun submitDireccion() {
        val state = _uiState.value

        if (state.calle.isBlank() || state.ciudad.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Calle y ciudad son requeridas.") }
            return
        }

        saveDireccionUseCase(
            Direccion(
                id = state.id,
                usuarioId = state.usuarioId,
                calle = state.calle,
                ciudad = state.ciudad,
                referencia = state.referencia
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
