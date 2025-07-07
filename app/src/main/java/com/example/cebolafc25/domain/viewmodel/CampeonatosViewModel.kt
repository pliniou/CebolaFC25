package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.data.model.JogadorEntity
import com.example.cebolafc25.data.repository.CampeonatoRepository
import com.example.cebolafc25.data.repository.JogadorRepository
import com.example.cebolafc25.domain.model.TipoCampeonato
import com.example.cebolafc25.domain.usecase.CreateCampeonatoUseCase
import com.example.cebolafc25.domain.usecase.GenerateMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class CampeonatoFormState(
    val nome: String = "",
    val tipo: TipoCampeonato = TipoCampeonato.PontosCorridos,
    val participantesSelecionados: Set<UUID> = emptySet() // IDs dos jogadores
)

@HiltViewModel
class CampeonatosViewModel @Inject constructor(
    private val createCampeonatoUseCase: CreateCampeonatoUseCase,
    private val generateMatchesUseCase: GenerateMatchesUseCase, // Injetando o novo UseCase
    campeonatoRepository: CampeonatoRepository,
    jogadorRepository: JogadorRepository
) : ViewModel() {
    
    val campeonatos = campeonatoRepository.getAllCampeonatos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    
    val jogadores = jogadorRepository.getAllJogadores()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val _formState = MutableStateFlow(CampeonatoFormState())
    val formState: StateFlow<CampeonatoFormState> = _formState.asStateFlow()
    
    val tiposDeCampeonato: List<TipoCampeonato> = TipoCampeonato.getAllTypes()

    fun onNomeChange(nome: String) {
        _formState.update { it.copy(nome = nome) }
    }

    fun onTipoChange(tipo: TipoCampeonato) {
        _formState.update { it.copy(tipo = tipo) }
    }

    fun onParticipanteSelected(jogadorId: UUID, isSelected: Boolean) {
        _formState.update { currentState ->
            val newSelection = currentState.participantesSelecionados.toMutableSet()
            if (isSelected) {
                newSelection.add(jogadorId)
            } else {
                newSelection.remove(jogadorId)
            }
            currentState.copy(participantesSelecionados = newSelection)
        }
    }

    fun createCampeonato() {
        viewModelScope.launch {
            val currentForm = _formState.value
            if (currentForm.nome.isNotBlank() && currentForm.participantesSelecionados.size >= 2) {
                // Cria a entidade do campeonato
                val novoCampeonato = com.example.cebolafc25.data.model.CampeonatoEntity(
                    nome = currentForm.nome.trim(),
                    tipo = currentForm.tipo.displayName,
                    dataCriacao = java.time.LocalDate.now()
                )
                
                // Insere no banco
                createCampeonatoUseCase(novoCampeonato.nome, novoCampeonato.tipo)

                // Gera as partidas para o campeonato recém-criado
                generateMatchesUseCase(novoCampeonato.id, currentForm.participantesSelecionados.toList())

                // Reseta o formulário
                _formState.value = CampeonatoFormState(tipo = currentForm.tipo)
            }
        }
    }
}