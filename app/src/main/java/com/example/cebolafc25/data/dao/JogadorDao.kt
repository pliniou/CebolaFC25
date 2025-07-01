package com.example.cebolafc25.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cebolafc25.data.model.JogadorEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface JogadorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJogador(jogador: JogadorEntity)

    @Update
    suspend fun updateJogador(jogador: JogadorEntity)

    @Query("SELECT * FROM jogadores WHERE id = :jogadorId")
    fun getJogadorById(jogadorId: UUID): Flow<JogadorEntity?>

    @Query("SELECT * FROM jogadores ORDER BY nome ASC")
    fun getAllJogadores(): Flow<List<JogadorEntity>>

    @Query("DELETE FROM jogadores WHERE id = :jogadorId")
    suspend fun deleteJogadorById(jogadorId: UUID)
}