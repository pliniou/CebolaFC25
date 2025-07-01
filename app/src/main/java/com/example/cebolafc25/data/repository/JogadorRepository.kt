package com.example.cebolafc25.data.repository

import com.example.cebolafc25.data.model.JogadorEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface JogadorRepository {
    suspend fun insertJogador(jogador: JogadorEntity)
    fun getAllJogadores(): Flow<List<JogadorEntity>>
    suspend fun getJogadorById(id: UUID): JogadorEntity?
    fun getJogadorByIdFlow(id: UUID): Flow<JogadorEntity?>
}