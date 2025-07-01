package com.example.cebolafc25.domain.usecase

import com.example.cebolafc25.data.model.CampeonatoEntity
import com.example.cebolafc25.data.repository.CampeonatoRepository
import java.time.LocalDate
import javax.inject.Inject

class CreateCampeonatoUseCase @Inject constructor(
    private val repository: CampeonatoRepository
) {
    suspend operator fun invoke(nome: String, tipo: String) {
        if (nome.isNotBlank() && tipo.isNotBlank()) {
            val novoCampeonato = CampeonatoEntity(
                nome = nome.trim(),
                tipo = tipo.trim(),
                dataCriacao = LocalDate.now()
            )
            repository.insertCampeonato(novoCampeonato)
        }
    }
}