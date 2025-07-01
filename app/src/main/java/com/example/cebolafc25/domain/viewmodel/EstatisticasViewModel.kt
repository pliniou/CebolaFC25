package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.data.repository.JogadorRepository
import com.example.cebolafc25.data.repository.PartidaRepository
import com.example.cebolafc25.domain.model.EstatisticasJogador
import com.example.cebolafc25.util.StatsCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EstatisticasViewModel @Inject constructor(
    jogadorRepository: JogadorRepository,
    partidaRepository: PartidaRepository
) : ViewModel() {

    val estatisticas: StateFlow<List<EstatisticasJogador>> =
        combine(
            jogadorRepository.getAllJogadores(),
            partidaRepository.getAllPartidas()
        ) { jogadores, partidas ->
            StatsCalculator.calculate(jogadores, partidas)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}