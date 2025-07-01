package com.example.cebolafc25.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cebolafc25.data.model.ParticipanteCampeonato
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ParticipanteCampeonatoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipante(participante: ParticipanteCampeonato)

    @Query("SELECT jogadorId FROM ParticipanteCampeonato WHERE campeonatoId = :campeonatoId")
    fun getParticipantesByCampeonato(campeonatoId: UUID): Flow<List<UUID>>

    @Query("DELETE FROM ParticipanteCampeonato WHERE campeonatoId = :campeonatoId AND jogadorId = :jogadorId")
    suspend fun deleteParticipante(campeonatoId: UUID, jogadorId: UUID)
}