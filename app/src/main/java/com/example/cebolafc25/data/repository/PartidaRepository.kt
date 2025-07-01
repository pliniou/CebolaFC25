package com.example.cebolafc25.data.repository

import com.example.cebolafc25.data.model.PartidaEntity
import kotlinx.coroutines.flow.Flow

interface PartidaRepository {
    suspend fun insertPartidaWithValidation(partida: PartidaEntity)
    fun getAllPartidas(): Flow<List<PartidaEntity>>
}