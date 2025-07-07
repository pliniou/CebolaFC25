package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.data.model.CampeonatoEntity
import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.data.repository.CampeonatoRepository
import com.example.cebolafc25.data.repository.JogadorRepository
import com.example.cebolafc25.data.repository.PartidaRepository
import com.example.cebolafc25.domain.model.EstatisticasJogador
import com.example.cebolafc25.domain.model.UiState
import com.example.cebolafc25.navigation.TOURNAMENT_ID_ARG
import com.example.cebolafc25.util.StatsCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

// O Estado da UI para a tela de detalhes permanece o mesmo
data class CampeonatoDetalhesState(
    val campeonato: CampeonatoEntity? = null,
    val partidas: List<PartidaEntity> = emptyList(),
    val classificacao: List<EstatisticasJogador> = emptyList(),
    val todosJogadores: Map<UUID, String> = emptyMap()
)

@HiltViewModel
class CampeonatoDetalhesViewModel @Inject constructor(
    campeonatoRepository: CampeonatoRepository,
    partidaRepository: PartidaRepository,
    jogadorRepository: JogadorRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tournamentId: String = savedStateHandle.get<String>(TOURNAMENT_ID_ARG) ?: ""

    private val _uiState = MutableStateFlow<UiState<CampeonatoDetalhesState>>(UiState.Loading)
    val uiState: StateFlow<UiState<CampeonatoDetalhesState>> = _uiState.asStateFlow()

    init {
        if (tournamentId.isNotBlank()) {
            val tournamentUuid = UUID.fromString(tournamentId)

            // CORREÇÃO: A lógica agora está dentro de viewModelScope.launch
            viewModelScope.launch {
                combine(
                    campeonatoRepository.getCampeonatoById(tournamentUuid),
                    partidaRepository.getPartidasByCampeonatoId(tournamentUuid),
                    jogadorRepository.getAllJogadores()
                ) { campeonato, partidas, todosJogadores ->

                    if (campeonato == null) {
                        throw IllegalStateException("Campeonato não encontrado")
                    }

                    val participantesIds = partidas.flatMap { listOf(it.jogador1Id, it.jogador2Id) }.toSet()
                    val participantes = todosJogadores.filter { it.id in participantesIds }

                    val partidasFinalizadas = partidas.filter { it.partidaFinalizada }
                    val classificacao = StatsCalculator.calculate(participantes, partidasFinalizadas)

                    val mapaJogadores = todosJogadores.associateBy({ it.id }, { it.nome })

                    UiState.Success(
                        CampeonatoDetalhesState(
                            campeonato = campeonato,
                            partidas = partidas,
                            classificacao = classificacao,
                            todosJogadores = mapaJogadores
                        )
                    )
                }.catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Erro ao carregar detalhes do campeonato.")
                }.collect { state ->
                    _uiState.value = state
                }
            }
        } else {
            _uiState.value = UiState.Error("ID do campeonato inválido.")
        }
    }
}