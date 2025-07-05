package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.data.model.CampeonatoEntity
import com.example.cebolafc25.data.repository.CampeonatoRepository
import com.example.cebolafc25.navigation.TOURNAMENT_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CampeonatoDetalhesViewModel @Inject constructor(
    campeonatoRepository: CampeonatoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tournamentId: String = savedStateHandle.get<String>(TOURNAMENT_ID_ARG) ?: ""

    val campeonato: StateFlow<CampeonatoEntity?> =
        campeonatoRepository.getCampeonatoById(UUID.fromString(tournamentId))
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
}