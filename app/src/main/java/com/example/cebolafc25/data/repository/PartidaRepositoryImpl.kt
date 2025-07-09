package com.example.cebolafc25.data.repository

import com.example.cebolafc25.data.dao.PartidaDao
import com.example.cebolafc25.data.model.PartidaEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class PartidaRepositoryImpl @Inject constructor(
    private val partidaDao: PartidaDao
) : PartidaRepository {
    override suspend fun insertPartidaWithValidation(partida: PartidaEntity) = partidaDao.insertWithValidation(partida)
    override fun getAllPartidas(): Flow<List<PartidaEntity>> = partidaDao.getAllPartidas()
    
    // NOVO: Implementação para buscar amistosos
    override fun getAmistosos(): Flow<List<PartidaEntity>> = partidaDao.getAmistosos()
    
    override fun getPartidasByCampeonatoId(campeonatoId: UUID): Flow<List<PartidaEntity>> = partidaDao.getPartidasByCampeonatoId(campeonatoId)
    override fun getPartidaById(partidaId: UUID): Flow<PartidaEntity?> = partidaDao.getPartidaById(partidaId)
    override suspend fun updatePartida(partida: PartidaEntity) = partidaDao.updatePartida(partida)
}