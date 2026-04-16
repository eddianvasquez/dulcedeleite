package edu.ucne.dulcedeleite.presentation.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.dulcedeleite.data.local.datastore.AuthTokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val tokenManager: AuthTokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    init {
        tokenManager.getNombre().onEach { nombre ->
            _uiState.update { it.copy(nombre = nombre ?: "Usuario") }
        }.launchIn(viewModelScope)

        tokenManager.getCorreo().onEach { correo ->
            _uiState.update { it.copy(correo = correo ?: "Correo no disponible") }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: PerfilUiEvent) {
        when (event) {
            PerfilUiEvent.CerrarSesion -> {
                viewModelScope.launch {
                    tokenManager.clearSession()
                    _logoutEvent.emit(Unit)
                }
            }
        }
    }
}
