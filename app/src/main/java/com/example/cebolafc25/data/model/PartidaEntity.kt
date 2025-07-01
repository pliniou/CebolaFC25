package com.example.cebolafc25.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "partidas")
data class PartidaEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(), // ID único para a partida
    val data: LocalDate, // Data da partida
    val jogador1Id: UUID, // ID do Jogador 1
    val jogador2Id: UUID, // ID do Jogador 2
    val time1Nome: String, // Nome do time do Jogador 1
    val time2Nome: String, // Nome do time do Jogador 2
    val placar1: Int, // Placar do Jogador 1
    val placar2: Int // Placar do Jogador 2
)