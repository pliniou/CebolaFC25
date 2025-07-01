package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.R
import com.example.cebolafc25.data.model.JogadorEntity
import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.data.repository.JogadorRepository
import com.example.cebolafc25.data.repository.PartidaRepository
import com.example.cebolafc25.domain.usecase.RegisterPartidaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class PartidaFormState(
    val jogador1Id: UUID? = null,
    val jogador2Id: UUID? = null,
    val time1Nome: String = "",
    val time2Nome: String = "",
    val placar1: String = "",
    val placar2: String = ""
) {
    val isFormValid: Boolean
        get() = jogador1Id != null &&
                jogador2Id != null &&
                jogador1Id != jogador2Id &&
                time1Nome.isNotBlank() &&
                time2Nome.isNotBlank() &&
                placar1.toIntOrNull() != null &&
                placar2.toIntOrNull() != null
}

sealed interface PartidaFormEvent {
    data class UpdateJogador1(val id: UUID?) : PartidaFormEvent
    data class UpdateJogador2(val id: UUID?) : PartidaFormEvent
    data class UpdateTime1(val name: String) : PartidaFormEvent
    data class UpdateTime2(val name: String) : PartidaFormEvent
    data class UpdatePlacar1(val score: String) : PartidaFormEvent
    data class UpdatePlacar2(val score: String) : PartidaFormEvent
    data object RegisterPartida : PartidaFormEvent
}

sealed interface UiEvent {
    data class ShowSnackbar(val messageResId: Int, val args: List<Any> = emptyList()) : UiEvent
}

@HiltViewModel
class PartidasViewModel @Inject constructor(
    partidaRepository: PartidaRepository,
    jogadorRepository: JogadorRepository,
    private val registerPartidaUseCase: RegisterPartidaUseCase
) : ViewModel() {

    val partidas: StateFlow<List<PartidaEntity>> = partidaRepository.getAllPartidas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val jogadores: StateFlow<List<JogadorEntity>> = jogadorRepository.getAllJogadores()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val _formState = MutableStateFlow(PartidaFormState())
    val formState: StateFlow<PartidaFormState> = _formState.asStateFlow()

    private val _eventChannel = Channel<UiEvent>()
    val uiEvent = _eventChannel.receiveAsFlow()

    fun onEvent(event: PartidaFormEvent) {
        when (event) {
            is PartidaFormEvent.UpdateJogador1 -> _formState.update { it.copy(jogador1Id = event.id) }
            is PartidaFormEvent.UpdateJogador2 -> _formState.update { it.copy(jogador2Id = event.id) }
            is PartidaFormEvent.UpdateTime1 -> _formState.update { it.copy(time1Nome = event.name) }
            is PartidaFormEvent.UpdateTime2 -> _formState.update { it.copy(time2Nome = event.name) }
            is PartidaFormEvent.UpdatePlacar1 -> _formState.update { it.copy(placar1 = event.score.filter { c -> c.isDigit() }) }
            is PartidaFormEvent.UpdatePlacar2 -> _formState.update { it.copy(placar2 = event.score.filter { c -> c.isDigit() }) }
            is PartidaFormEvent.RegisterPartida -> registerPartida()
        }
    }

    private fun registerPartida() {
        val currentState = _formState.value
        if (!currentState.isFormValid) {
            viewModelScope.launch {
                _eventChannel.send(UiEvent.ShowSnackbar(R.string.partida_vm_fill_fields_correctly))
            }
            return
        }

        viewModelScope.launch {
            // Uso de let para garantir null-safety sem o operador '!!'
            val p1 = currentState.placar1.toIntOrNull()
            val p2 = currentState.placar2.toIntOrNull()

            if (currentState.jogador1Id != null && currentState.jogador2Id != null && p1 != null && p2 != null) {
                try {
                    registerPartidaUseCase(
                        jogador1Id = currentState.jogador1Id,
                        jogador2Id = currentState.jogador2Id,
                        time1Nome = currentState.time1Nome,
                        time2Nome = currentState.time2Nome,
                        placar1 = p1,
                        placar2 = p2
                    )
                    _formState.value = PartidaFormState() // Reset form
                    _eventChannel.send(UiEvent.ShowSnackbar(R.string.partida_vm_register_success))
                } catch (e: Exception) {
                    val errorMessage = e.message ?: "Unknown error"
                    _eventChannel.send(UiEvent.ShowSnackbar(R.string.partida_vm_register_error, listOf(errorMessage)))
                }
            }
        }
    }
}