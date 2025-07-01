package com.example.cebolafc25.domain.usecase

import com.example.cebolafc25.data.model.JogadorEntity
import com.example.cebolafc25.data.repository.JogadorRepository
import javax.inject.Inject

class AddJogadorUseCase @Inject constructor(
    private val repository: JogadorRepository
) {
    suspend operator fun invoke(nome: String) {
        if (nome.isNotBlank()) {
            val novoJogador = JogadorEntity(nome = nome.trim())
            repository.insertJogador(novoJogador)
        }
    }
}