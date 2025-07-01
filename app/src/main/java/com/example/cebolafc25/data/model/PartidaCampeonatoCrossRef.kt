package com.example.cebolafc25.data.model

import androidx.room.Entity
import java.util.UUID

@Entity(primaryKeys = ["campeonatoId", "partidaId"])
data class PartidaCampeonatoCrossRef(
    val campeonatoId: UUID, // ID do campeonato
    val partidaId: UUID // ID da partida associada ao campeonato
)