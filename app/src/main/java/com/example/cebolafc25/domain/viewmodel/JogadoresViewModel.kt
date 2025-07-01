package com.example.cebolafc25.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cebolafc25.data.model.JogadorEntity
import com.example.cebolafc25.data.repository.JogadorRepository
import com.example.cebolafc25.domain.model.UiState
import com.example.cebolafc25.domain.usecase.AddJogadorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JogadoresViewModel @Inject constructor(
    private val addJogadorUseCase: AddJogadorUseCase,
    jogadorRepository: JogadorRepository
) : ViewModel() {

    val jogadoresState: StateFlow<UiState<List<JogadorEntity>>> = jogadorRepository.getAllJogadores()
        .map<List<JogadorEntity>, UiState<List<JogadorEntity>>> { jogadores ->
            // CORREÇÃO: Removido o cast 'as'. O tipo de retorno do operador .map é definido
            // explicitamente, o que permite ao .catch emitir um UiState.Error sem conflito de tipos.
            // Isso torna o código mais seguro e legível.
            UiState.Success(jogadores)
        }
        .catch { e ->
            emit(UiState.Error(e.message ?: "Erro ao carregar a lista de jogadores."))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    private val _novoJogadorNome = MutableStateFlow("")
    val novoJogadorNome: StateFlow<String> = _novoJogadorNome.asStateFlow()

    fun onNomeChange(nome: String) {
        _novoJogadorNome.value = nome
    }

    fun addJogador() {
        viewModelScope.launch {
            // A lógica de negócio está corretamente encapsulada no UseCase.
            addJogadorUseCase(novoJogadorNome.value)
            _novoJogadorNome.value = "" // Limpa o campo após adicionar para melhor UX.
        }
    }
}