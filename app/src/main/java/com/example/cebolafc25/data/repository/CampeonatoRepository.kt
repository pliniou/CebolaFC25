package com.example.cebolafc25.data.repository

import com.example.cebolafc25.data.model.CampeonatoEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CampeonatoRepository {
    suspend fun insertCampeonato(campeonato: CampeonatoEntity)
    fun getAllCampeonatos(): Flow<List<CampeonatoEntity>>
    fun getCampeonatoById(campeonatoId: UUID): Flow<CampeonatoEntity?>
}