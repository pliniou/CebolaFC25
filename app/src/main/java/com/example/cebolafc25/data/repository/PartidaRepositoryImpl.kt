package com.example.cebolafc25.data.repository

import com.example.cebolafc25.data.dao.PartidaDao
import com.example.cebolafc25.data.model.PartidaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PartidaRepositoryImpl @Inject constructor(
    private val partidaDao: PartidaDao
) : PartidaRepository {
    override suspend fun insertPartidaWithValidation(partida: PartidaEntity) = partidaDao.insertWithValidation(partida)
    override fun getAllPartidas(): Flow<List<PartidaEntity>> = partidaDao.getAllPartidas()
}