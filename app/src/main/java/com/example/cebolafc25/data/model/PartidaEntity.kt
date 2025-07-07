package com.example.cebolafc25.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "partidas")
data class PartidaEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val data: LocalDate,
    val jogador1Id: UUID,
    val jogador2Id: UUID,
    val time1Nome: String,
    val time2Nome: String,
    val placar1: Int,
    val placar2: Int,
    // NOVO CAMPO: ID do campeonato (opcional, para partidas amistosas)
    val campeonatoId: UUID? = null, 
    // NOVO CAMPO: Indica se a partida já teve o resultado registrado
    val partidaFinalizada: Boolean = false 
)