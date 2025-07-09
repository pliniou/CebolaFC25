package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.data.repository.JogadorRepository
import com.example.cebolafc25.data.repository.PartidaRepository
import com.example.cebolafc25.data.repository.TeamRepository
import com.example.cebolafc25.domain.model.UiState
import com.example.cebolafc25.domain.usecase.UpdatePartidaUseCase
import com.example.cebolafc25.navigation.MATCH_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class PartidaDetalhesState(
    val partida: PartidaEntity? = null,
    val nomeJogador1: String = "",
    val nomeJogador2: String = "",
    val placar1: String = "",
    val placar2: String = "",
    val time1Nome: String = "",
    val time2Nome: String = "",
    val isFinalizada: Boolean = false
)

@HiltViewModel
class PartidaDetalhesViewModel @Inject constructor(
    private val partidaRepository: PartidaRepository,
    private val jogadorRepository: JogadorRepository,
    val teamRepository: TeamRepository,
    private val updatePartidaUseCase: UpdatePartidaUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val matchId: String = savedStateHandle.get<String>(MATCH_ID_ARG) ?: ""
    private val _uiState = MutableStateFlow<UiState<PartidaDetalhesState>>(UiState.Loading)
    val uiState: StateFlow<UiState<PartidaDetalhesState>> = _uiState.asStateFlow()

    init {
        loadPartidaDetails()
    }

    private fun loadPartidaDetails() {
        if (matchId.isBlank()) {
            _uiState.value = UiState.Error("ID da partida não encontrado.")
            return
        }
        viewModelScope.launch {
            try {
                // Usar .first() para obter o valor atual do Flow e encerrá-lo
                val partida = partidaRepository.getPartidaById(UUID.fromString(matchId)).first()
                if (partida == null) {
                    _uiState.value = UiState.Error("Partida não encontrada.")
                    return@launch
                }

                // Usar suspend function para obter os jogadores
                val jogador1 = jogadorRepository.getJogadorById(partida.jogador1Id)
                val jogador2 = jogadorRepository.getJogadorById(partida.jogador2Id)

                val initialState = PartidaDetalhesState(
                    partida = partida,
                    nomeJogador1 = jogador1?.nome ?: "??",
                    nomeJogador2 = jogador2?.nome ?: "??",
                    placar1 = if (partida.partidaFinalizada) partida.placar1.toString() else "",
                    placar2 = if (partida.partidaFinalizada) partida.placar2.toString() else "",
                    time1Nome = partida.time1Nome,
                    time2Nome = partida.time2Nome,
                    isFinalizada = partida.partidaFinalizada
                )
                _uiState.value = UiState.Success(initialState)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Erro ao carregar partida.")
            }
        }
    }

    fun onPlacar1Change(value: String) {
        updateState { it.copy(placar1 = value.filter { c -> c.isDigit() }) }
    }

    fun onPlacar2Change(value: String) {
        updateState { it.copy(placar2 = value.filter { c -> c.isDigit() }) }
    }

    fun onTime1Change(value: String) {
        updateState { it.copy(time1Nome = value) }
    }

    fun onTime2Change(value: String) {
        updateState { it.copy(time2Nome = value) }
    }

    fun savePartida() {
        if (_uiState.value !is UiState.Success) return
        val currentState = (_uiState.value as UiState.Success<PartidaDetalhesState>).data

        viewModelScope.launch {
            val placar1 = currentState.placar1.toIntOrNull()
            val placar2 = currentState.placar2.toIntOrNull()
            val partidaOriginal = currentState.partida

            // Validação para garantir que os campos obrigatórios foram preenchidos
            if (placar1 != null && placar2 != null && partidaOriginal != null &&
                currentState.time1Nome.isNotBlank() && currentState.time1Nome != "A definir" &&
                currentState.time2Nome.isNotBlank() && currentState.time2Nome != "A definir") {
                
                val partidaAtualizada = partidaOriginal.copy(
                    placar1 = placar1,
                    placar2 = placar2,
                    time1Nome = currentState.time1Nome,
                    time2Nome = currentState.time2Nome,
                    partidaFinalizada = true // Marcar como finalizada ao salvar
                )
                updatePartidaUseCase(partidaAtualizada)
            } else {
                // Opcional: Enviar um evento de erro para a UI se a validação falhar
            }
        }
    }

    private fun updateState(updateAction: (PartidaDetalhesState) -> PartidaDetalhesState) {
        _uiState.update {
            if (it is UiState.Success) {
                UiState.Success(updateAction(it.data))
            } else {
                it
            }
        }
    }
}