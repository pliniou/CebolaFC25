package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.data.repository.JogadorRepository
import com.example.cebolafc25.data.repository.PartidaRepository
import com.example.cebolafc25.data.repository.TeamRepository
import com.example.cebolafc25.domain.usecase.UpdatePartidaUseCase
import com.example.cebolafc25.navigation.MATCH_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

// CORREÇÃO: Adicionado 'val' a todas as propriedades da data class.
data class PartidaDetalhesState(
    val partida: PartidaEntity? = null,
    val nomeJogador1: String = "",
    val nomeJogador2: String = "",
    val placar1: String = "",
    val placar2: String = "",
    val liga1: String = "",
    val liga2: String = "",
    val time1Nome: String = "",
    val time2Nome: String = "",
    val isFinalizada: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class PartidaDetalhesViewModel @Inject constructor(
    private val partidaRepository: PartidaRepository,
    private val jogadorRepository: JogadorRepository,
    val teamRepository: TeamRepository,
    private val updatePartidaUseCase: UpdatePartidaUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val matchId: UUID? = savedStateHandle.get<String>(MATCH_ID_ARG)?.let { UUID.fromString(it) }

    private val _uiState = MutableStateFlow(PartidaDetalhesState())
    val uiState: StateFlow<PartidaDetalhesState> = _uiState.asStateFlow()

    init {
        loadPartidaDetails()
    }

    private fun loadPartidaDetails() {
        if (matchId == null) {
            _uiState.value = PartidaDetalhesState(isLoading = false)
            return
        }

        viewModelScope.launch {
            try {
                partidaRepository.getPartidaById(matchId)
                    .filterNotNull()
                    .collectLatest { partida ->
                        val jogador1 = jogadorRepository.getJogadorById(partida.jogador1Id)
                        val jogador2 = jogadorRepository.getJogadorById(partida.jogador2Id)
                        val liga1 = teamRepository.getLeagueForTeam(partida.time1Nome) ?: teamRepository.getLeagues().firstOrNull() ?: ""
                        val liga2 = teamRepository.getLeagueForTeam(partida.time2Nome) ?: teamRepository.getLeagues().firstOrNull() ?: ""

                        _uiState.value = PartidaDetalhesState(
                            partida = partida,
                            nomeJogador1 = jogador1?.nome ?: "??",
                            nomeJogador2 = jogador2?.nome ?: "??",
                            placar1 = if (partida.partidaFinalizada) partida.placar1.toString() else "",
                            placar2 = if (partida.partidaFinalizada) partida.placar2.toString() else "",
                            liga1 = liga1,
                            liga2 = liga2,
                            time1Nome = partida.time1Nome,
                            time2Nome = partida.time2Nome,
                            isFinalizada = partida.partidaFinalizada,
                            isLoading = false
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = PartidaDetalhesState(isLoading = false)
            }
        }
    }

    fun onPlacar1Change(value: String) {
        _uiState.update { it.copy(placar1 = value.filter { c -> c.isDigit() }) }
    }

    fun onPlacar2Change(value: String) {
        _uiState.update { it.copy(placar2 = value.filter { c -> c.isDigit() }) }
    }

    fun onLiga1Change(value: String) {
        _uiState.update { it.copy(liga1 = value, time1Nome = "") }
    }

    fun onLiga2Change(value: String) {
        _uiState.update { it.copy(liga2 = value, time2Nome = "") }
    }

    fun onTime1Change(value: String) {
        _uiState.update { it.copy(time1Nome = value) }
    }

    fun onTime2Change(value: String) {
        _uiState.update { it.copy(time2Nome = value) }
    }

    fun savePartida() {
        val currentState = _uiState.value
        viewModelScope.launch {
            val placar1 = currentState.placar1.toIntOrNull()
            val placar2 = currentState.placar2.toIntOrNull()
            val partidaOriginal = currentState.partida

            if (placar1 != null && placar2 != null && partidaOriginal != null &&
                currentState.time1Nome.isNotBlank() && currentState.time1Nome != "A definir" &&
                currentState.time2Nome.isNotBlank() && currentState.time2Nome != "A definir") {

                val partidaAtualizada = partidaOriginal.copy(
                    placar1 = placar1,
                    placar2 = placar2,
                    time1Nome = currentState.time1Nome,
                    time2Nome = currentState.time2Nome,
                    partidaFinalizada = true
                )
                updatePartidaUseCase(partidaAtualizada)
            }
        }
    }
}