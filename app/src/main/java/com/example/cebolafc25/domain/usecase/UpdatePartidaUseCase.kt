package com.example.cebolafc25.domain.usecase

import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.data.repository.PartidaRepository
import javax.inject.Inject

class UpdatePartidaUseCase @Inject constructor(
    private val repository: PartidaRepository
) {
    suspend operator fun invoke(partida: PartidaEntity) {
        repository.updatePartida(partida)
    }
}