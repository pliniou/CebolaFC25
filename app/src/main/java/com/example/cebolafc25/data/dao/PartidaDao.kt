package com.example.cebolafc25.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.cebolafc25.data.model.PartidaEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

@Dao
interface PartidaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartida(partida: PartidaEntity)

    @Transaction
    suspend fun insertWithValidation(partida: PartidaEntity) {
        if (partida.jogador1Id == partida.jogador2Id) {
            throw IllegalArgumentException("Os jogadores devem ser diferentes.")
        }
        insertPartida(partida)
    }

    @Update
    suspend fun updatePartida(partida: PartidaEntity)

    @Query("SELECT * FROM partidas WHERE id = :partidaId")
    fun getPartidaById(partidaId: UUID): Flow<PartidaEntity?>

    @Query("SELECT * FROM partidas WHERE campeonatoId = :campeonatoId ORDER BY data DESC")
    fun getPartidasByCampeonatoId(campeonatoId: UUID): Flow<List<PartidaEntity>>

    @Query("SELECT * FROM partidas ORDER BY data DESC, id DESC")
    fun getAllPartidas(): Flow<List<PartidaEntity>>

    @Query("SELECT * FROM partidas WHERE jogador1Id = :jogadorId OR jogador2Id = :jogadorId ORDER BY data DESC")
    fun getPartidasByJogador(jogadorId: UUID): Flow<List<PartidaEntity>>

    @Query("DELETE FROM partidas WHERE id = :partidaId")
    suspend fun deletePartidaById(partidaId: UUID)
}