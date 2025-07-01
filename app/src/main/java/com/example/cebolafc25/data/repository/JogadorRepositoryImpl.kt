package com.example.cebolafc25.data.repository

import com.example.cebolafc25.data.dao.JogadorDao
import com.example.cebolafc25.data.model.JogadorEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class JogadorRepositoryImpl @Inject constructor(
    private val jogadorDao: JogadorDao
) : JogadorRepository {
    override suspend fun insertJogador(jogador: JogadorEntity) = jogadorDao.insertJogador(jogador)
    override fun getAllJogadores(): Flow<List<JogadorEntity>> = jogadorDao.getAllJogadores()
    override suspend fun getJogadorById(id: UUID): JogadorEntity? = jogadorDao.getJogadorById(id).firstOrNull()
    override fun getJogadorByIdFlow(id: UUID): Flow<JogadorEntity?> = jogadorDao.getJogadorById(id)
}