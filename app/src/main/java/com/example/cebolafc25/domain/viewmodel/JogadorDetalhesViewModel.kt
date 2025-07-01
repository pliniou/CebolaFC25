package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.data.repository.JogadorRepository
import com.example.cebolafc25.data.repository.PartidaRepository
import com.example.cebolafc25.domain.model.EstatisticasJogador
import com.example.cebolafc25.domain.model.UiState
import com.example.cebolafc25.navigation.PLAYER_ID_ARG
import com.example.cebolafc25.util.StatsCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class JogadorDetalhesViewModel @Inject constructor(
    jogadorRepository: JogadorRepository,
    partidaRepository: PartidaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val playerId: String = savedStateHandle.get<String>(PLAYER_ID_ARG) ?: ""

    val jogadorDetalhesState: StateFlow<UiState<EstatisticasJogador>> =
        if (playerId.isNotBlank()) {
            combine(
                jogadorRepository.getJogadorByIdFlow(UUID.fromString(playerId)),
                partidaRepository.getAllPartidas()
            ) { jogador, partidas ->
                if (jogador == null) {
                    throw IllegalStateException("Jogador não encontrado com o ID: $playerId")
                }
                // A lógica de cálculo permanece a mesma, agora encapsulada em StatsCalculator
                StatsCalculator.calculate(listOf(jogador), partidas).first()
            }
                .map<EstatisticasJogador, UiState<EstatisticasJogador>> { estatisticas ->
                    // CORREÇÃO: Removido o cast 'as'. O tipo é explicitamente definido no operador 'map',
                    // tornando o código mais seguro e legível.
                    UiState.Success(estatisticas)
                }
                .catch { e ->
                    emit(UiState.Error(e.message ?: "Erro desconhecido ao carregar detalhes."))
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = UiState.Loading
                )
        } else {
            // Estado de erro explícito se o ID não for fornecido, evitando crashes.
            MutableStateFlow<UiState<EstatisticasJogador>>(UiState.Error("ID do jogador não fornecido.")).stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = UiState.Error("ID do jogador não fornecido.")
            )
        }
}