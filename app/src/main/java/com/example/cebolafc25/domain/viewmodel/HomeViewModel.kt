package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.data.model.JogadorEntity
import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.data.repository.JogadorRepository
import com.example.cebolafc25.data.repository.PartidaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    partidaRepository: PartidaRepository,
    val jogadorRepository: JogadorRepository
) : ViewModel() {

    val ultimasPartidas: StateFlow<List<PartidaEntity>> =
        partidaRepository.getAllPartidas().map { it.take(3) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val jogadores: StateFlow<List<JogadorEntity>> = jogadorRepository.getAllJogadores()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}