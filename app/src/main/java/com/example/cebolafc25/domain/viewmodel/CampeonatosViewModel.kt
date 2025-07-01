package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.data.repository.CampeonatoRepository
import com.example.cebolafc25.domain.model.TipoCampeonato
import com.example.cebolafc25.domain.usecase.CreateCampeonatoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CampeonatoFormState(
    val nome: String = "",
    val tipo: TipoCampeonato = TipoCampeonato.PontosCorridos // Valor padrão
)

@HiltViewModel
class CampeonatosViewModel @Inject constructor(
    private val createCampeonatoUseCase: CreateCampeonatoUseCase,
    campeonatoRepository: CampeonatoRepository
) : ViewModel() {

    val campeonatos = campeonatoRepository.getAllCampeonatos()
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

    fun createCampeonato() {
        viewModelScope.launch {
            val currentForm = _formState.value
            if (currentForm.nome.isNotBlank()) {
                createCampeonatoUseCase(currentForm.nome, currentForm.tipo.displayName)
                // Reseta o formulário, mantendo o último tipo selecionado para conveniência do usuário
                _formState.value = CampeonatoFormState(tipo = currentForm.tipo)
            }
        }
    }
}