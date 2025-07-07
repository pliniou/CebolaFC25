package com.example.cebolafc25.data.repository

import com.example.cebolafc25.data.model.PartidaEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface PartidaRepository {
    suspend fun insertPartidaWithValidation(partida: PartidaEntity)
    fun getAllPartidas(): Flow<List<PartidaEntity>>
    fun getPartidasByCampeonatoId(campeonatoId: UUID): Flow<List<PartidaEntity>>
    fun getPartidaById(partidaId: UUID): Flow<PartidaEntity?>
    suspend fun updatePartida(partida: PartidaEntity)
}