package com.example.cebolafc25.data.model

import androidx.room.Entity
import java.util.UUID

@Entity(primaryKeys = ["campeonatoId", "jogadorId"])
data class ParticipanteCampeonato(
    val campeonatoId: UUID, // ID do campeonato
    val jogadorId: UUID // ID do jogador participante
)