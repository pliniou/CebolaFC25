package com.example.cebolafc25.data.repository

import com.example.cebolafc25.data.dao.CampeonatoDao
import com.example.cebolafc25.data.model.CampeonatoEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class CampeonatoRepositoryImpl @Inject constructor(
    private val campeonatoDao: CampeonatoDao
) : CampeonatoRepository {
    override suspend fun insertCampeonato(campeonato: CampeonatoEntity) = campeonatoDao.insertCampeonato(campeonato)
    override fun getAllCampeonatos(): Flow<List<CampeonatoEntity>> = campeonatoDao.getAllCampeonatos()
    override fun getCampeonatoById(campeonatoId: UUID): Flow<CampeonatoEntity?> = campeonatoDao.getCampeonatoById(campeonatoId)
}