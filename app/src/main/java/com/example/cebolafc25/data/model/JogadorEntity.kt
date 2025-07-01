package com.example.cebolafc25.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "jogadores")
data class JogadorEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(), // ID único para o jogador
    val nome: String // Nome do jogador
)