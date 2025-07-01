package com.example.cebolafc25.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "campeonatos")
data class CampeonatoEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(), // ID único para o campeonato
    val nome: String, // Nome do campeonato
    val tipo: String, // Tipo do campeonato (Pontos Corridos, Mata-mata, etc.)
    val dataCriacao: LocalDate // Data de criação do campeonato
)